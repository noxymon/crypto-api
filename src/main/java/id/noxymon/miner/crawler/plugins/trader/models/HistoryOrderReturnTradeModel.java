package id.noxymon.miner.crawler.plugins.trader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class HistoryOrderReturnTradeModel {
    @JsonProperty("trade_id")
    private String tradeId;
    @JsonProperty("order_id")
    private String orderId;
    private String type;
    private String btc;
    private String eth;
    private String price;
    private String fee;
    @JsonProperty("trade_time")
    private String tradeTime;
}
