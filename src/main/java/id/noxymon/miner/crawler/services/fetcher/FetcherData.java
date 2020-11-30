package id.noxymon.miner.crawler.services.fetcher;

import java.time.LocalDateTime;

public interface FetcherData {
    void fetchData(String cryptoCurrency, LocalDateTime now);
}
