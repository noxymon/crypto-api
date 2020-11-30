package id.noxymon.miner.crawler.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.annotation.Documented;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HmacUtilTest {

    @Test
    public void testHmac(){
        String testParam = "method=getInfo&timestamp=1578304294000&recvWindow=1578303937000";
        String testSecret = "f60617a68fcce028f0a90bc9eb765d17379eb548cc935c01a7ee3186eecf870e9b68f27a31bcfe8d";
        final String hmacResult = HmacUtil.from(testParam, testSecret);
        Assertions.assertEquals(
                "bab004e5a518740d7a33b38b44dbebecd3fb39f40b42391af39fcce06edabff5233b3e8064a07c528d1c751a6923d5116026c7786e01b22e2d35277a098cae99",
                hmacResult
        );
    }

    @Test
    public void anuan(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("type", "buy");
        map.add("method", "trade");
        map.add("price", "1000");
        map.add("timestamp", "1606058160000");
        map.add("idr", "10000");
        map.add("recvWindow", "1606058180000");
        map.add("pair", "eth_idr");

        final UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://example.com")
                .queryParams(map)
                .build();
        System.out.println(uriComponents.getQuery());
    }
}