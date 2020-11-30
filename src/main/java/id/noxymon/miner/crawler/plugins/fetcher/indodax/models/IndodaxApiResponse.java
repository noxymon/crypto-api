package id.noxymon.miner.crawler.plugins.fetcher.indodax.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class IndodaxApiResponse {
    @JsonProperty("s")
    private String status;
    @JsonProperty("t")
    private List<Long> timestamp = Collections.emptyList();
    @JsonProperty("c")
    private List<Double> closePrice = Collections.emptyList();
    @JsonProperty("o")
    private List<Double> openPrice = Collections.emptyList();
    @JsonProperty("h")
    private List<Double> highPrice = Collections.emptyList();
    @JsonProperty("l")
    private List<Double> lowPrice = Collections.emptyList();
    @JsonProperty("v")
    private List<Double> volume = Collections.emptyList();
}
