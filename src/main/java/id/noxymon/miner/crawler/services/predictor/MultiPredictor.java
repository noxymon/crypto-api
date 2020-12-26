package id.noxymon.miner.crawler.services.predictor;

import id.noxymon.miner.crawler.services.predictor.enums.TimeUnitEnum;
import weka.classifiers.evaluation.NumericPrediction;

import java.util.List;

public interface MultiPredictor {
    List<NumericPrediction> predictFutureOf(TimeUnitEnum unitTimes, int step) throws Exception;
}
