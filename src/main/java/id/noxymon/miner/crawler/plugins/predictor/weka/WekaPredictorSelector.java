package id.noxymon.miner.crawler.plugins.predictor.weka;

import id.noxymon.miner.crawler.repository.EtheriumMinutesRepository;
import id.noxymon.miner.crawler.repository.entities.TbEth;
import id.noxymon.miner.crawler.services.predictor.MultiPredictor;
import id.noxymon.miner.crawler.services.predictor.enums.DaysUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.enums.HoursUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.enums.MinutesUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.enums.TimeUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.models.PricePrediction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.*;
import weka.filters.supervised.attribute.TSLagMaker;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class WekaPredictorSelector implements MultiPredictor {

    private final ZoneId zone = ZoneId.of("Asia/Jakarta");
    private final Attribute low = new Attribute("low");
    private final Attribute open = new Attribute("open");
    private final Attribute high = new Attribute("high");
    private final Attribute close = new Attribute("close");
    private final Attribute date = new Attribute("date", "yyyy-MM-dd'T'HH:mm");
    private final ArrayList<Attribute> attributeList  = new ArrayList<>(Arrays.asList(date, open, high, low, close));

    private final EtheriumMinutesRepository etheriumMinutesRepository;

    @Autowired
    public WekaPredictorSelector(EtheriumMinutesRepository etheriumMinutesRepository) {
        this.etheriumMinutesRepository = etheriumMinutesRepository;
    }

    @Override
    public List<PricePrediction> predictFutureOf(TimeUnitPredictor unitTimes, int step) throws Exception {
        List<TbEth> historicalData = getHistoricalData(unitTimes);
        Instances dataset = composeDataset(unitTimes.getUnitTime(), historicalData);

        LinearRegression linearRegression = buildLinearRegression(dataset);

        WekaForecaster forecaster = buildForecaster(dataset, linearRegression, unitTimes.getLagMakerMaxLag());
        forecaster.primeForecaster(dataset);

        return composeNumericPredictionList(step, forecaster, unitTimes);
    }

    private List<TbEth> getHistoricalData(TimeUnitPredictor unitTimes) {
        if(unitTimes instanceof MinutesUnitPredictor){
            return fetchHistoricalData((MinutesUnitPredictor) unitTimes);
        }
        if(unitTimes instanceof HoursUnitPredictor){
            return fetchHistoricalData((HoursUnitPredictor) unitTimes);
        }
        if(unitTimes instanceof DaysUnitPredictor){
            return fetchHistoricalData((DaysUnitPredictor) unitTimes);
        }
        throw new RuntimeException("Not Detected Unit Time !!");
    }

    private List<TbEth> fetchHistoricalData(MinutesUnitPredictor unitTimes) {
        return etheriumMinutesRepository.findRecordMinutePrediction(
                Timestamp.valueOf(LocalDateTime.now()),
                unitTimes.getMaxHistoricalDataInterval(),
                unitTimes.getUnitTime());
    }

    private List<TbEth> fetchHistoricalData(DaysUnitPredictor unitTimes) {
        return etheriumMinutesRepository.findRecordDailyPrediction(
                Timestamp.valueOf(LocalDateTime.now()),
                unitTimes.getMaxHistoricalDataInterval(),
                unitTimes.getUnitTime());
    }

    private List<TbEth> fetchHistoricalData(HoursUnitPredictor unitTimes) {
        return etheriumMinutesRepository.findRecordHourPrediction(
                Timestamp.valueOf(LocalDateTime.now()),
                unitTimes.getMaxHistoricalDataInterval(),
                unitTimes.getUnitTime());
    }

    private WekaForecaster buildForecaster(Instances dataset, LinearRegression linearRegression, int max) throws Exception {
        WekaForecaster forecaster = new WekaForecaster();
        forecaster.reset();
        TSLagMaker lagMaker = forecaster.getTSLagMaker();
        lagMaker.setTimeStampField("date");
        lagMaker.setMinLag(1);
        lagMaker.setMaxLag(max);
        lagMaker.setAddDayOfWeek(true);
        lagMaker.setAddDayOfMonth(true);
        lagMaker.setAddNumDaysInMonth(true);
        lagMaker.setAddQuarterOfYear(true);
        lagMaker.setAddWeekendIndicator(true);
        lagMaker.setAddMonthOfYear(true);
        forecaster.setFieldsToForecast("open,high,low,close");
        forecaster.setBaseForecaster(linearRegression);
        forecaster.buildForecaster(dataset, System.out);
        return forecaster;
    }

    private LinearRegression buildLinearRegression(Instances dataset) throws Exception {
        LinearRegression linearRegression = new LinearRegression();
        linearRegression.setMinimal(false);
        linearRegression.setEliminateColinearAttributes(false);
        linearRegression.setAttributeSelectionMethod(new SelectedTag(1, LinearRegression.TAGS_SELECTION));
        linearRegression.buildClassifier(dataset);
        return linearRegression;
    }

    private List<PricePrediction> composeNumericPredictionList(int step, WekaForecaster forecaster,
                                                               TimeUnitPredictor timeUnit) throws Exception {
        TSLagMaker tsLagMaker = forecaster.getTSLagMaker();
        long currentTimeStampValue = (long)tsLagMaker.getCurrentTimeStampValue();
        log.info("epoch in ms : " + currentTimeStampValue);

        List<PricePrediction> numericPredictionList = new ArrayList<>();
        for (List<NumericPrediction> numericPredictions : forecaster.forecast(step, System.out)) {
            long advancedTimestampEpoch = getAdvancedTimestampEpoch(tsLagMaker, currentTimeStampValue);
            LocalDateTime advancedLocalDateTime = getLocalDateTimeFromEpoch(advancedTimestampEpoch);

            PricePrediction pricePrediction = new PricePrediction(
                    numericPredictions
                            .get(3)
                            .predicted(),
                    advancedLocalDateTime
                            .truncatedTo(ChronoUnit.MINUTES)
                            .minusMinutes(timeUnit.getCorrectionTime())
            );
            numericPredictionList.add(pricePrediction);

            currentTimeStampValue = advancedTimestampEpoch;
        }
        return numericPredictionList;
    }

    private long getAdvancedTimestampEpoch(TSLagMaker tsLagMaker, long currentTimeStampValue) {
        return Double.valueOf(tsLagMaker.advanceSuppliedTimeValue(currentTimeStampValue)).longValue();
    }

    private LocalDateTime getLocalDateTimeFromEpoch(long advancedTimestampEpoch) {
        return Instant.ofEpochMilli(advancedTimestampEpoch)
                .atZone(zone)
                .toLocalDateTime()
                .truncatedTo(ChronoUnit.MINUTES);
    }

    private Instances composeDataset(int unitMinutes, List<TbEth> recordInAvergaeMinute) {
        Instances dataset = new Instances(unitMinutes +" minutes etherium", attributeList, recordInAvergaeMinute.size());
        dataset.setClass(close);
        recordInAvergaeMinute
                .forEach(averagePerMinute -> {
                    log.info("Retrieve data for " + averagePerMinute.getTbeDate().toString());
                    Instance minuteInstance = mapRecordToInstance(averagePerMinute);
                    dataset.add(minuteInstance);
                });
        return dataset;
    }

    private Instance mapRecordToInstance(TbEth averagePerMinute) {
        Instance minuteInstance = new SparseInstance(5);
        try {
            minuteInstance.setValue(attributeList.get(0), date.parseDate(averagePerMinute.getTbeDate().toLocalDateTime().toString()));
            minuteInstance.setValue(attributeList.get(1), averagePerMinute.getTbeOpen().doubleValue());
            minuteInstance.setValue(attributeList.get(2), averagePerMinute.getTbeHigh().doubleValue());
            minuteInstance.setValue(attributeList.get(3), averagePerMinute.getTbeLow().doubleValue());
            minuteInstance.setValue(attributeList.get(4), averagePerMinute.getTbeClose().doubleValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return minuteInstance;
    }
}
