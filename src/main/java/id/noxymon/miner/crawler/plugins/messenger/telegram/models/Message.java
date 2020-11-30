package id.noxymon.miner.crawler.plugins.messenger.telegram.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Message {
    @JsonProperty("update_id")
    private Integer updateId;
    @JsonProperty("message")
    private MessageDetail messageDetail;
}
