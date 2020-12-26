package id.noxymon.miner.crawler.plugins.predictor.weka;

import id.noxymon.miner.crawler.plugins.fetcher.indodax.IndodaxApiFetcher;
import id.noxymon.miner.crawler.services.predictor.enums.MinutesUnitPredictor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import weka.classifiers.evaluation.NumericPrediction;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class WekaPredictorSelectorIntegrationTest {

    @Autowired
    private IndodaxApiFetcher indodaxApiFetcher;

    @Autowired
    private WekaPredictorSelector wekaPredictorSelector;

    @Test
    @Disabled
    public void forecastTest_5minute() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2020,12,12,11,0);
        LocalDateTime endTime = LocalDateTime.now();

        List<NumericPrediction> numericPredictionList = wekaPredictorSelector.predictFutureOf(MinutesUnitPredictor.FIVE_MINUTES, 1);
        System.out.println(numericPredictionList.get(0).predicted());
    }

}