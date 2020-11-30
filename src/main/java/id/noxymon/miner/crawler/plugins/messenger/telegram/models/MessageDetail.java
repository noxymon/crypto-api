package id.noxymon.miner.crawler.plugins.messenger.telegram.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageDetail {
    @JsonProperty("message_id")
    private Integer messageId;
    private Sender from;
    private MessageContent chat;
    private Integer date;
    private String text;
}
