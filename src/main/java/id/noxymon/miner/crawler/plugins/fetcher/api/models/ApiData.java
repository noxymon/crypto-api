package id.noxymon.miner.crawler.plugins.fetcher.api.models;

import lombok.Getter;

import java.util.List;

@Getter
public class ApiData {
    private String iso;
    private String name;
    private String slug;
    private String interval;
    private List<List<Double>> entries;
}
