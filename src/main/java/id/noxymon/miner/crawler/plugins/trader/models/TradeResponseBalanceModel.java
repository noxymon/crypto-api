package id.noxymon.miner.crawler.plugins.trader.models;

import lombok.Getter;

@Getter
public class TradeResponseBalanceModel {
    private String idr;
    private String btc;
    private String frozenIdr;
    private String frozenBtc;
}
