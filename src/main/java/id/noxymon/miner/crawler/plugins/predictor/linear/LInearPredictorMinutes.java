package id.noxymon.miner.crawler.plugins.predictor.linear;

import id.noxymon.miner.crawler.repository.EtheriumMinutesRepository;
import id.noxymon.miner.crawler.repository.EtheriumModelLinearRepository;
import id.noxymon.miner.crawler.repository.entities.TbEth;
import id.noxymon.miner.crawler.repository.entities.TbModelLinear;
import id.noxymon.miner.crawler.services.predictor.Predictor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinearPredictorMinutes implements Predictor {

    private static final int MAX_HISTORICAL_RECORD = 720;
    private final EtheriumMinutesRepository minutesRepository;
    private final EtheriumModelLinearRepository modelLinearRepository;

    @Override
    public double predictFutureOf(LocalDateTime futureTime) {
        LocalDateTime startHistoricalDateTime = futureTime.truncatedTo(ChronoUnit.MINUTES).minusHours(MAX_HISTORICAL_RECORD);
        LocalDateTime previousHour = futureTime.minusMinutes(1).truncatedTo(ChronoUnit.MINUTES);

        final List<TbEth> minutesRepositoryList = minutesRepository.findAllByTbeDateBetweenOrderByTbeDateDesc(
                Timestamp.valueOf(startHistoricalDateTime),
                Timestamp.valueOf(previousHour)
        );
        return predictFutureValueFromDataset(minutesRepositoryList);
    }

    private double predictFutureValueFromDataset(List<TbEth> historicalDataset) {
        final DescriptiveStatistics descriptiveStatistics = getDescriptiveStatistics(historicalDataset);
        final List<TbModelLinear> modelLinear = modelLinearRepository.findAllByCoefficientInAndAndIndexIn(generateCoefficientList(), generateIndexList());
        
        double futureValue = 0d;
        AtomicInteger counter = new AtomicInteger(1);
        for (TbEth etheriumHistoricalHour : historicalDataset) {
            int currentIndex = counter.getAndIncrement();
            double openCoefficientValue = getOpenAverageValueOf(currentIndex, modelLinear);
            double highCoefficientValue = getHighAverageValueOf(currentIndex, modelLinear);
            double lowCoefficientValue = getLowAverageValueOf(currentIndex, modelLinear);
            double closeCoefficientValue = getCloseAverageValueOf(currentIndex, modelLinear);

            double currentValue = openCoefficientValue * normalizedValueOf(etheriumHistoricalHour.getTbeOpen().doubleValue(), descriptiveStatistics) +
                    highCoefficientValue * normalizedValueOf(etheriumHistoricalHour.getTbeHigh().doubleValue(), descriptiveStatistics) +
                    lowCoefficientValue * normalizedValueOf(etheriumHistoricalHour.getTbeLow().doubleValue(), descriptiveStatistics) +
                    closeCoefficientValue * normalizedValueOf(etheriumHistoricalHour.getTbeClose().doubleValue(), descriptiveStatistics);
            futureValue = futureValue + currentValue;
        }
        return denormalizedValueOf(futureValue + getCoefficientValue(modelLinear), descriptiveStatistics);
    }

    private List<Integer> generateIndexList() {
        return IntStream.rangeClosed(0, MAX_HISTORICAL_RECORD)
                .boxed().collect(Collectors.toList());
    }

    private List<String> generateCoefficientList() {
        return Arrays.asList("low", "high", "open", "close", "coefficient");
    }

    private double getOpenAverageValueOf(int currentIndex, List<TbModelLinear> modelLinearList) {
        return getAverageValueOf(currentIndex, "open", modelLinearList);
    }

    private double getHighAverageValueOf(int currentIndex, List<TbModelLinear> modelLinearList) {
        return getAverageValueOf(currentIndex, "high", modelLinearList);
    }

    private double getLowAverageValueOf(int currentIndex, List<TbModelLinear> modelLinearList) {
        return getAverageValueOf(currentIndex, "low", modelLinearList);
    }

    private double getCloseAverageValueOf(int currentIndex, List<TbModelLinear> modelLinearList) {
        return getAverageValueOf(currentIndex, "close", modelLinearList);
    }

    private double getCoefficientValue(List<TbModelLinear> modelLinearList) {
        return getAverageValueOf(0, "coefficient", modelLinearList);
    }

    private Double getAverageValueOf(Integer currentIndex, String coefficientName, List<TbModelLinear> modelLinearList) {
        final Optional<TbModelLinear> modelLinear = modelLinearList.parallelStream()
                .filter(modelLinearItem ->
                        coefficientName.equals(modelLinearItem.getCoefficient()) &&
                        currentIndex.equals(modelLinearItem.getIndex()))
                .findAny();
        if (modelLinear.isPresent()) {
            return modelLinear.get().getValue();
        }
        return 0d;
    }
    
    private double denormalizedValueOf(double normalizedValue, DescriptiveStatistics stats){
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();
        return (normalizedValue*std)+mean;
    }

    private double normalizedValueOf(double value, DescriptiveStatistics stats){
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();
        return (value - mean)/std;
    }

    private DescriptiveStatistics getDescriptiveStatistics(List<TbEth> historicalDataset) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        historicalDataset.parallelStream()
                .forEach(tbEth -> {
                    stats.addValue(tbEth.getTbeClose().doubleValue());
                });
        return stats;
    }
}
