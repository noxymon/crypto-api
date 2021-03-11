package id.noxymon.miner.crawler.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "application.crawler.enable=false",
        "spring.datasource.database=h2",
        "spring.jpa.hibernate.ddl-auto=create-drop"})
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
        map.add("method", "trade");
        map.add("timestamp", "1606058160000");
        map.add("recvWindow", "1606058180000");
        map.add("pair", "xrp_idr");
        map.add("type", "sell");
        map.add("xrp", "1.65111111");
        map.add("price", "7000");

        final UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("https://indodax.com/tapi")
                .queryParams(map)
                .build();
        final String hmacResult = HmacUtil.from(uriComponents.getQuery(), "f60617a68fcce028f0a90bc9eb765d17379eb548cc935c01a7ee3186eecf870e9b68f27a31bcfe8d");
        System.out.println(hmacResult);
//        System.out.println(uriComponents.getQuery());
    }
}