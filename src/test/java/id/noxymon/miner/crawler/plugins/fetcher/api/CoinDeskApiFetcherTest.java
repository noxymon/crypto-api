package id.noxymon.miner.crawler.plugins.fetcher.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class CoinDeskApiFetcherTest {

    @Autowired
    private CoinDeskApiFetcher coinDeskApiFetcher;

    @Test
    public void testApi_thenSaveToDatabase(){
        coinDeskApiFetcher.fetchData("ETH", LocalDateTime.of(2020,11,17,3,0));
    }

    @Test
    public void generateDataFrom() throws InterruptedException {
        coinDeskApiFetcher.fetchDataOfBetween("ETH", LocalDateTime.of(2020,11,17,2,0), LocalDateTime.now());
    }
}