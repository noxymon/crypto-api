package id.noxymon.miner.crawler.plugins.predictor.linear;

import id.noxymon.miner.crawler.services.predictor.Predictor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class LinearPredictorHourTest {

    @Autowired
    @Qualifier("linearPredictorHour")
    private Predictor predictor;

    @Test
    public void testNearestPreviousTime(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousHour = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.HOURS);
        System.out.println(previousHour);
    }

    @Test
    public void testData(){
        LocalDateTime testFutureTime = LocalDateTime.of(2020, 11, 22,20, 00);
        final double futureValue = predictor.predictFutureOf(testFutureTime);
        Assertions.assertEquals(7668000.0000, futureValue);
    }
}