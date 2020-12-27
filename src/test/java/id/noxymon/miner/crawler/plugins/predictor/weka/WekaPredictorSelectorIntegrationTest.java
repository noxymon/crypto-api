package id.noxymon.miner.crawler.plugins.predictor.weka;

import id.noxymon.miner.crawler.services.predictor.enums.MinutesUnitPredictor;
import id.noxymon.miner.crawler.services.predictor.models.PricePrediction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WekaPredictorSelectorIntegrationTest {

    @Autowired
    private WekaPredictorSelector wekaPredictorSelector;

    @Test
    @Disabled
    public void forecastTest_5minute() throws Exception {
        List<PricePrediction> numericPredictionList = wekaPredictorSelector.predictFutureOf(MinutesUnitPredictor.FIVE_MINUTES, 10);
        for (PricePrediction pricePrediction : numericPredictionList) {
            System.out.println(pricePrediction.getFutureTimestamp() + " : " + pricePrediction.getPredictedPrice());
        }
    }

}