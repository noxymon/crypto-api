package id.noxymon.miner.crawler.plugins.fetcher.summarizer;

import id.noxymon.miner.crawler.plugins.fetcher.summarizer.HourSummarizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class HourSummarizerTest {

    @Autowired
    private HourSummarizer hourSummarizer;

    @Test
    public void summarize(){
        final LocalDateTime targetTime = LocalDateTime.of(2020,11,22,23,59).truncatedTo(ChronoUnit.HOURS);
        final LocalDateTime startTime = LocalDateTime.of(2020,7,1,00,00).truncatedTo(ChronoUnit.HOURS);
        hourSummarizer.summarize(startTime,targetTime);
    }
}