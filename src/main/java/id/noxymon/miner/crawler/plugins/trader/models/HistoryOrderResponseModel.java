package id.noxymon.miner.crawler.plugins.trader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class HistoryOrderResponseModel {
    @JsonProperty("return")
    private HistoryOrderReturnModel mReturn;
    private Integer success;
}
