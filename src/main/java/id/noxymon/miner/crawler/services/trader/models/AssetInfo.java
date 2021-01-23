package id.noxymon.miner.crawler.services.trader.models;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetInfo {
    private Double moneyBalance = 0d;
    private List<BitcoinBalanceInfo> bitCoinList= Collections.emptyList();
}
