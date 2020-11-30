package id.noxymon.miner.crawler.plugins.trader.models;

import lombok.Getter;

@Getter
public class TradeResponseReturnModel {
    private String receiveBtc;
    private Integer spendRp;
    private Integer fee;
    private Integer remainRp;
    private Integer orderId;
    private TradeResponseBalanceModel balance;
}
