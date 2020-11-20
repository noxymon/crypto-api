package id.noxymon.miner.crawler.learn;

import id.noxymon.miner.crawler.services.predictor.LinearPredictor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class TestLearn {

    @Autowired
    private LinearPredictor linearPredictor;

    @Test
    public void testNearestPreviousTime(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousHour = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.HOURS);
        System.out.println(previousHour);
    }

    @Test
    public void testData(){
        LocalDateTime testFutureTime = LocalDateTime.of(2020, 10, 2,11, 0);
        final double futureValue = linearPredictor.predictFutureOf(testFutureTime);
        Assertions.assertEquals(349.29588830774867, futureValue);
    }
}
