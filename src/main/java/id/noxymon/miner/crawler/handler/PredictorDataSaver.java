package id.noxymon.miner.crawler.handler;

import id.noxymon.miner.crawler.repository.EtheriumPredictionRepository;
import id.noxymon.miner.crawler.repository.entities.TbEthPrediction;
import id.noxymon.miner.crawler.services.predictor.MultiPredictor;
import id.noxymon.miner.crawler.services.predictor.enums.DaysUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.enums.HoursUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.enums.MinutesUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.models.PricePrediction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Slf4j
@Component
@RequiredArgsConstructor
public class PredictorDataSaver {

    private final MultiPredictor predictor;
    private final EtheriumPredictionRepository etheriumPredictionRepository;

    public void updatePrediction() {
        try {
            updateFiveMinutesPrediction();
            updateTenMinutesPrediction();
            updateFifteenMinutesPrediction();

            updateOneHourPrediction();
            updateThreeHourPrediction();
            updateSixHourPrediction();
            updateTwelveHourPrediction();
            updateTwentyFourHourPrediction();

            updateThreeDaysPrediction();
            updateSevenDaysPrediction();
            updateFourteenDaysPrediction();
            updateThirtyDaysPrediction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateThirtyDaysPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(DaysUnitPredictor.THIRTY_DAYS, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted30Days(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateFourteenDaysPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(DaysUnitPredictor.FOURTEEN_DAYS, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted14Days(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateSevenDaysPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(DaysUnitPredictor.SEVEN_DAYS, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted7Days(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateThreeDaysPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(DaysUnitPredictor.THREE_DAYS, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted3Days(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateTwentyFourHourPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(HoursUnitPredictor.TWENTY_FOUR_HOUR, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted24Hour(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateTwelveHourPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(HoursUnitPredictor.TWELVE_HOUR, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted12Hour(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateSixHourPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(HoursUnitPredictor.SIX_HOUR, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted6Hour(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        };
    }

    private void updateThreeHourPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(HoursUnitPredictor.THREE_HOUR, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted3Hour(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateOneHourPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(HoursUnitPredictor.ONE_HOUR, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted1Hour(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateFifteenMinutesPrediction() throws Exception {
        for (PricePrediction pricePrediction : predictor.predictFutureOf(MinutesUnitPredictor.FIFTEEN_MINUTES, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted15Minutes(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateTenMinutesPrediction() throws Exception {
        log.info("Predict 10 minutes Time frame");
        for (PricePrediction pricePrediction : predictor.predictFutureOf(MinutesUnitPredictor.TEN_MINUTES, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted10Minutes(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }

    private void updateFiveMinutesPrediction() throws Exception {
        log.info("Predict 5 minutes Time frame");
        for (PricePrediction pricePrediction : predictor.predictFutureOf(MinutesUnitPredictor.FIVE_MINUTES, 1)) {
            TbEthPrediction etheriumPricePredicted = etheriumPredictionRepository
                    .findById(Timestamp.valueOf(pricePrediction.getFutureTimestamp()))
                    .orElseGet(() -> {
                        TbEthPrediction newEtheriumPrediction = new TbEthPrediction();
                        newEtheriumPrediction.setPredictedTimestamp(Timestamp.valueOf(pricePrediction.getFutureTimestamp()));
                        return newEtheriumPrediction;
                    });
            etheriumPricePredicted.setPredicted5Minutes(pricePrediction.getPredictedPrice());
            etheriumPredictionRepository.saveAndFlush(etheriumPricePredicted);
        }
    }
}
