package id.noxymon.miner.crawler.services.predictor.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PricePrediction {
    private final Double predictedPrice;
    private final LocalDateTime futureTimestamp;
}
