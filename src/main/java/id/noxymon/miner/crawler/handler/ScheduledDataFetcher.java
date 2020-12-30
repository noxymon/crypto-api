package id.noxymon.miner.crawler.handler;

import id.noxymon.miner.crawler.services.fetcher.FetcherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "application.crawler.enable", havingValue = "true")
public class ScheduledDataFetcher {

    @Autowired
    @Qualifier("indodaxApiFetcher")
    private FetcherData fetcherData;

    @Value("${application.prediction.enable}")
    private boolean enablePrediction;

    private final PredictorDataSaver predictorDataSaver;

    @Scheduled(cron = "${application.crawler.cron}")
    public void execute(){
        fetcherData.fetchData("ETHIDR", LocalDateTime.now());

        if(enablePrediction){
            predictorDataSaver.updatePrediction();
        }
    }
}
