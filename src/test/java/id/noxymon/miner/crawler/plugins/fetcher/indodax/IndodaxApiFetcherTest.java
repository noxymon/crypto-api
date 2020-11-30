package id.noxymon.miner.crawler.plugins.fetcher.indodax;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class IndodaxApiFetcherTest {

    @Autowired
    private IndodaxApiFetcher apiFetcher;

    @Test
    public void generateData(){
        LocalDateTime startTime = LocalDateTime.of(2020,11,24,05,00);
        LocalDateTime endTime = LocalDateTime.now();
        apiFetcher.fetchData("ETHIDR", startTime, endTime);
    }
}