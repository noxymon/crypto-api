package id.noxymon.miner.crawler.plugins.messenger.telegram.models;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class ApiResponse {
    private Boolean ok;
    private List<Message> result = Collections.emptyList();
}
