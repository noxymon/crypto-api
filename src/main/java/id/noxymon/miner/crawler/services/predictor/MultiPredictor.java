package id.noxymon.miner.crawler.services.predictor;

import id.noxymon.miner.crawler.services.predictor.enums.TimeUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.models.PricePrediction;

import java.util.List;

public interface MultiPredictor {
    List<PricePrediction> predictFutureOf(TimeUnitPredictor unitTimes, int step) throws Exception;
}
