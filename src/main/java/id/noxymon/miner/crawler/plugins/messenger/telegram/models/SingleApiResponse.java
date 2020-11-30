package id.noxymon.miner.crawler.plugins.messenger.telegram.models;

import lombok.Data;

@Data
public class SingleApiResponse {
    private Boolean ok;
    private Message result;
}
