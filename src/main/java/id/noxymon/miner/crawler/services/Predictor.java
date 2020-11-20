package id.noxymon.miner.crawler.services;

import java.time.LocalDateTime;

public interface Predictor {
    double predictFutureOf(LocalDateTime futureTime);
}
