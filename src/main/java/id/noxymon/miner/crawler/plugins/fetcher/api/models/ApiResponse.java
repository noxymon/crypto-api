package id.noxymon.miner.crawler.plugins.fetcher.api.models;

import lombok.Getter;

@Getter
public class ApiResponse {
    private int statusCode;
    private String message;
    private ApiData data;
}
