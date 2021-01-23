package id.noxymon.miner.crawler.services.trader.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BitcoinBalanceInfo {
    private String bitCoinName;
    private Double bitCoinBalance;
}
