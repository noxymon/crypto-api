package id.noxymon.miner.crawler.plugins.trader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AssetInfoResponseModel {
    public Integer success;
    @JsonProperty("return")
    public AssetInfoReturnModel mReturn;
}
