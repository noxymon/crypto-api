package id.noxymon.miner.crawler.plugins.trader;

import id.noxymon.miner.crawler.plugins.trader.models.TradeResponseModel;
import id.noxymon.miner.crawler.services.trader.Trader;
import id.noxymon.miner.crawler.utils.HmacUtil;
import id.noxymon.miner.crawler.utils.QueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndodaxTrader implements Trader {

    private final RestTemplate restTemplate;

    private ZoneId zone = ZoneId.of("Asia/Jakarta");

    @Value("${application.trader.indodax.url}")
    private String privateApi;
    @Value("${application.trader.indodax.secret}")
    private String indodaxSecret;
    @Value("${application.trader.indodax.key}")
    private String indodaxKey;

    @Override
    public void buyCoin(String coinName, String currency, Double amountMoneyToBuy, Double currentPrice) {
        HttpEntity<MultiValueMap<String, String>> request = composeBuyRequest(coinName, currency, amountMoneyToBuy, currentPrice);

        final TradeResponseModel tradeResponseModel = restTemplate.postForObject(privateApi, request, TradeResponseModel.class);
        if(tradeResponseModel.getSuccess().equals(1)){
           return;
        }
        throw new RuntimeException("Error On Buying Coin !!");
    }

    private HttpEntity<MultiValueMap<String, String>> composeBuyRequest(String coinName, String currency,
                                                                        Double amountMoneyToBuy, Double currentPrice)
    {
        final Long nowInEpoch = LocalDateTime.now().toEpochSecond(zone.getRules().getOffset(LocalDateTime.now()));
        final Long expiredTimeInEpoch = nowInEpoch + 5000L;

        MultiValueMap<String, String> map = composeBuyBodyMap(coinName, currency, amountMoneyToBuy, currentPrice,
                nowInEpoch, expiredTimeInEpoch);
        String queryParam = QueryUtil.getQueryFrom(map);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Key", indodaxKey);
        httpHeaders.set("Sign", HmacUtil.from(queryParam, indodaxSecret));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(map, httpHeaders);
    }

    private MultiValueMap<String, String> composeBuyBodyMap(String coinName, String currency, Double amountMoneyToBuy, Double currentPrice, Long nowInEpoch, Long expiredTimeInEpoch) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("type", "buy");
        map.add("method", "trade");
        map.add("price", currentPrice.toString());
        map.add("timestamp", nowInEpoch.toString());
        map.add("idr", amountMoneyToBuy.toString());
        map.add("recvWindow", expiredTimeInEpoch.toString());
        map.add("pair", coinName.toLowerCase() +"_"+ currency.toLowerCase());
        return map;
    }

    @Override
    public void sellCoin(String coinName, String currency, Double amountCoinToSell, Double currentPrice) {
        final HttpEntity<MultiValueMap<String, String>> request = composeSellRequest(coinName, currency, amountCoinToSell, currentPrice);
        final TradeResponseModel tradeResponseModel = restTemplate.postForObject(privateApi, request, TradeResponseModel.class);
        if(tradeResponseModel.getSuccess().equals(1)){
            return;
        }
        throw new RuntimeException("Error On Selling Coin !!");
    }

    private HttpEntity<MultiValueMap<String, String>> composeSellRequest(String coinName, String currency,
                                                                         Double amountCoinToSell, Double currentPrice) {
        final Long nowInEpoch = LocalDateTime.now().toEpochSecond(zone.getRules().getOffset(LocalDateTime.now()));
        final Long expiredTimeInEpoch = nowInEpoch + 5000L;

        MultiValueMap<String, String> map = composeSellBodyMap(coinName, currency, amountCoinToSell, currentPrice,
                nowInEpoch, expiredTimeInEpoch);
        String queryParam = QueryUtil.getQueryFrom(map);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Key", indodaxKey);
        httpHeaders.set("Sign", HmacUtil.from(queryParam, indodaxSecret));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(map, httpHeaders);
    }

    private MultiValueMap<String, String> composeSellBodyMap(String coinName, String currency, Double amountCoinToSell, Double currentPrice, Long nowInEpoch, Long expiredTimeInEpoch) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("type", "sell");
        map.add("method", "trade");
        map.add("price", currentPrice.toString());
        map.add("timestamp", nowInEpoch.toString());
        map.add("btc", amountCoinToSell.toString());
        map.add("recvWindow", expiredTimeInEpoch.toString());
        map.add("pair", coinName.toLowerCase() +"_"+ currency.toLowerCase());
        return map;
    }
}
