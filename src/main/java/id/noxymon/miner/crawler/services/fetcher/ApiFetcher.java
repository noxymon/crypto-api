package id.noxymon.miner.crawler.services.fetcher;

import id.noxymon.miner.crawler.services.FetcherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiFetcher implements FetcherData {
    @Override
    public void fetchData() {
    }
}
