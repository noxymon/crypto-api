package id.noxymon.miner.crawler.services.predictor;

import java.time.LocalDateTime;

public interface Predictor {
    double predictFutureOf(LocalDateTime futureTime);
}
