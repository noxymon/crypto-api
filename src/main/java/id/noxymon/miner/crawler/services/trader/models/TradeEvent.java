package id.noxymon.miner.crawler.services.trader.models;

import java.time.LocalDateTime;
import java.util.Optional;

import id.noxymon.miner.crawler.utils.EventEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TradeEvent {
    private final EventEnum        event;
    private final LocalDateTime    eventTime;
    private final Optional<Double> eventPriceMoney;
    private final Optional<Double> eventPriceBitcoin;
}
