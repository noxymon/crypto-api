package id.noxymon.miner.crawler.plugins.predictor.weka;

import id.noxymon.miner.crawler.repository.EtheriumMinutesRepository;
import id.noxymon.miner.crawler.repository.entities.TbEth;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.timeseries.WekaForecaster;
import weka.classifiers.timeseries.eval.TSEvaluation;
import weka.core.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
public class wekaPredictorLearnTest {

    @Autowired
    private EtheriumMinutesRepository minutesRepository;

    @Test
    @Disabled
    public void queryFromDB_convertToInstance() throws Exception {
        final Attribute date = new Attribute("date", "yyyy-MM-dd'T'HH:mm");
        final Attribute avg_open = new Attribute("avg_open");
        final Attribute avg_high = new Attribute("avg_high");
        final Attribute avg_low = new Attribute("avg_low");
        final Attribute avg_close = new Attribute("avg_close");
        final int limit = 1280;
        final int lagSize = 32;

        final ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
        attributeList.add(date);
        attributeList.add(avg_open);
        attributeList.add(avg_high);
        attributeList.add(avg_low);
        attributeList.add(avg_close);

        final int count = (int) minutesRepository.count();
        Instances dataset = new Instances("5 minutes etherium", attributeList, count);
        dataset.setClass(avg_close);

        final List<TbEth> collect = minutesRepository
                .findRecordMinutePrediction(
                        Timestamp.valueOf(LocalDateTime.now()),
                        3,
                        5)
                .parallelStream()
                .collect(Collectors.toList());

        collect.forEach(eachMinute -> {
            try {
                log.info("Retrieve data for " + eachMinute.getTbeDate().toString());
                Instance minuteInstance = new SparseInstance(5);
                minuteInstance.setValue(attributeList.get(0), date.parseDate(eachMinute.getTbeDate().toLocalDateTime().toString()));
                minuteInstance.setValue(attributeList.get(1), eachMinute.getTbeOpen().doubleValue());
                minuteInstance.setValue(attributeList.get(2), eachMinute.getTbeHigh().doubleValue());
                minuteInstance.setValue(attributeList.get(3), eachMinute.getTbeLow().doubleValue());
                minuteInstance.setValue(attributeList.get(4), eachMinute.getTbeClose().doubleValue());

                dataset.add(minuteInstance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        LinearRegression linearRegression = new LinearRegression();
        linearRegression.setMinimal(false);
        linearRegression.setEliminateColinearAttributes(false);
        linearRegression.setAttributeSelectionMethod(new SelectedTag(1, LinearRegression.TAGS_SELECTION));
        linearRegression.buildClassifier(dataset);

        WekaForecaster forecaster = new WekaForecaster();
        forecaster.setFieldsToForecast("avg_open,avg_high,avg_low,avg_close");
        forecaster.setBaseForecaster(linearRegression);
        forecaster.getTSLagMaker().setTimeStampField("date");
        forecaster.getTSLagMaker().setMinLag(1);
        forecaster.getTSLagMaker().setMaxLag(lagSize);
        forecaster.getTSLagMaker().setAddDayOfWeek(true);
        forecaster.getTSLagMaker().setAddDayOfMonth(true);
        forecaster.getTSLagMaker().setAddNumDaysInMonth(true);
        forecaster.getTSLagMaker().setAddQuarterOfYear(true);
        forecaster.getTSLagMaker().setAddWeekendIndicator(true);
        forecaster.getTSLagMaker().setAddMonthOfYear(true);
        forecaster.buildForecaster(dataset, System.out);

        forecaster.primeForecaster(dataset);

        double currentTimeStampValue = forecaster.getTSLagMaker().getCurrentTimeStampValue();
        List<LocalDateTime> forecastedTimestampList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            final double v = forecaster.getTSLagMaker().advanceSuppliedTimeValue(currentTimeStampValue);
            final Date date1 = new Date((long) v);
            LocalDateTime forecastedTimestamp = date1.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            forecastedTimestampList.add(forecastedTimestamp);
            currentTimeStampValue = v;
        }

        TSEvaluation evaluation = new TSEvaluation(dataset, 0.4);
        evaluation.setHorizon(12);
        evaluation.setPrimeWindowSize(lagSize);
        evaluation.setEvaluationModules("MAE,MAPE,DAC,Error");
        evaluation.evaluateForecaster(forecaster, System.out);
        System.out.println(evaluation.toSummaryString());
    }
}
