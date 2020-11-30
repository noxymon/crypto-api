package id.noxymon.miner.crawler.plugins.messenger.telegram.models;

import lombok.Data;

@Data
public class Sender {
    private Integer id;
    private Boolean isBot;
    private String firstName;
    private String lastName;
    private String username;
    private String languageCode;
}
