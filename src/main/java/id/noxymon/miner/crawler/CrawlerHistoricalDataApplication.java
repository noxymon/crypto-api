package id.noxymon.miner.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication(proxyBeanMethods = false)
public class CrawlerHistoricalDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerHistoricalDataApplication.class, args);
    }

}
