package id.noxymon.miner.crawler.utils;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

public class QueryUtil {
    public static String getQueryFrom(MultiValueMap<String, String> map){
        return UriComponentsBuilder.fromHttpUrl("http://example.com")
                .queryParams(map)
                .build()
                .getQuery();
    }
}
