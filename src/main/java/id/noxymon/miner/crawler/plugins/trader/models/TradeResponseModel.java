package id.noxymon.miner.crawler.plugins.trader.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TradeResponseModel {
    private Integer success;
    @JsonProperty("return")
    private TradeResponseReturnModel returnModel;
}
