package id.noxymon.miner.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CrawlerHistoricalDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerHistoricalDataApplication.class, args);
    }

}
