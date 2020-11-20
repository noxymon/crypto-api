package id.noxymon.miner.crawler.services.predictor;

import id.noxymon.miner.crawler.repository.EtheriumHourlyRepository;
import id.noxymon.miner.crawler.repository.EtheriumModelLinearRepository;
import id.noxymon.miner.crawler.repository.entities.TbEthHour;
import id.noxymon.miner.crawler.repository.entities.TbModelLinear;
import id.noxymon.miner.crawler.services.Predictor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinearPredictor implements Predictor {

    private final EtheriumHourlyRepository hourlyRepository;
    private final EtheriumModelLinearRepository modelLinearRepository;

    private static final int MAX_HISTORICAL_HOUR = 2160;

    @Override
    public double predictFutureOf(LocalDateTime futureTime) {
        LocalDateTime startHistoricalDateTime = futureTime.truncatedTo(ChronoUnit.HOURS).minusHours(MAX_HISTORICAL_HOUR);
        LocalDateTime previousHour = futureTime.minusHours(1).truncatedTo(ChronoUnit.HOURS);

        AtomicInteger counter = new AtomicInteger(1);
        final List<TbEthHour> historicalDataset = hourlyRepository.findAllByTbDateBetweenOrderByTbDateDesc(
                Timestamp.valueOf(startHistoricalDateTime), Timestamp.valueOf(previousHour)
        );

        double futureValue = 0d;
        for (TbEthHour etheriumHistoricalHour : historicalDataset) {
            int currentIndex = counter.getAndIncrement();
            double openCoefficientValue = getOpenAverageValueOf(currentIndex);
            double highCoefficientValue = getHighAverageValueOf(currentIndex);
            double lowCoefficientValue = getLowAverageValueOf(currentIndex);
            double closeCoefficientValue = getCloseAverageValueOf(currentIndex);

            double currentValue = openCoefficientValue * etheriumHistoricalHour.getOpenAvg() +
                    highCoefficientValue * etheriumHistoricalHour.getHighAvg() +
                    lowCoefficientValue * etheriumHistoricalHour.getLowAvg() +
                    closeCoefficientValue * etheriumHistoricalHour.getCloseAvg();
            futureValue = futureValue + currentValue;
        }
        return futureValue + getCoefficientValue();
    }

    private double getOpenAverageValueOf(int currentIndex) {
        return getAverageValueOf(currentIndex, "open_avg");
    }

    private double getHighAverageValueOf(int currentIndex) {
        return getAverageValueOf(currentIndex, "high_avg");
    }

    private double getLowAverageValueOf(int currentIndex) {
        return getAverageValueOf(currentIndex, "low_avg");
    }

    private double getCloseAverageValueOf(int currentIndex) {
        return getAverageValueOf(currentIndex, "close_avg");
    }

    private double getCoefficientValue(){
        return getAverageValueOf(0, "coefficient");
    }

    private Double getAverageValueOf(int currentIndex, String coefficientName) {
        TbModelLinear.PrimaryKeys modelLinearPK = new TbModelLinear.PrimaryKeys();
        modelLinearPK.setCoefficient(coefficientName);
        modelLinearPK.setIndex(currentIndex);
        Optional<TbModelLinear> modelLinear = modelLinearRepository.findById(modelLinearPK);
        if(modelLinear.isPresent()){
            return modelLinear.get().getValue();
        }
        return 0d;
    }
}
