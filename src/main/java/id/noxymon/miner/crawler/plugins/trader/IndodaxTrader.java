package id.noxymon.miner.crawler.plugins.trader;

import static id.noxymon.miner.crawler.utils.NumberUtil.from;

import id.noxymon.miner.crawler.plugins.trader.models.AssetInfoResponseModel;
import id.noxymon.miner.crawler.plugins.trader.models.HistoryOrderResponseModel;
import id.noxymon.miner.crawler.plugins.trader.models.HistoryOrderReturnModel;
import id.noxymon.miner.crawler.plugins.trader.models.HistoryOrderReturnTradeModel;
import id.noxymon.miner.crawler.plugins.trader.models.TradeResponseModel;
import id.noxymon.miner.crawler.services.trader.Trader;
import id.noxymon.miner.crawler.services.trader.models.AssetInfo;
import id.noxymon.miner.crawler.services.trader.models.BitcoinBalanceInfo;
import id.noxymon.miner.crawler.services.trader.models.TradeEvent;
import id.noxymon.miner.crawler.utils.EventEnum;
import id.noxymon.miner.crawler.utils.HmacUtil;
import id.noxymon.miner.crawler.utils.NumberUtil;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        HttpEntity<MultiValueMap<String, String>> request = composeBuyRequest(
                coinName,
                currency,
                amountMoneyToBuy,
                currentPrice
        );

        final TradeResponseModel tradeResponseModel = restTemplate.postForObject(
                privateApi,
                request,
                TradeResponseModel.class
        );
        if (tradeResponseModel.getSuccess().equals(1)) {
            return;
        }
        throw new RuntimeException("Error On Buying Coin !!");
    }

    private HttpEntity<MultiValueMap<String, String>> composeBuyRequest(String coinName, String currency,
                                                                        Double amountMoneyToBuy, Double currentPrice) {
        final Long nowInEpoch = getCurrentDateTimeInEpochMilis();
        final Long expiredTimeInEpoch = nowInEpoch + 5000L;

        MultiValueMap<String, String> map = composeBuyBodyMap(coinName, currency, amountMoneyToBuy, currentPrice,
                nowInEpoch, expiredTimeInEpoch
        );
        return composeQueryParamWithCrendetial(map);
    }

    private MultiValueMap<String, String> composeBuyBodyMap(String coinName, String currency, Double amountMoneyToBuy
            , Double currentPrice, Long nowInEpoch, Long expiredTimeInEpoch) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("type", "buy");
        map.add("method", "trade");
        map.add("price", currentPrice.toString());
        map.add("timestamp", nowInEpoch.toString());
        map.add("idr", amountMoneyToBuy.toString());
        map.add("recvWindow", expiredTimeInEpoch.toString());
        map.add("pair", coinName.toLowerCase() + "_" + currency.toLowerCase());
        return map;
    }

    @Override
    public void sellCoin(String coinName, String currency, Double amountCoinToSell, Double currentPrice) {
        final HttpEntity<MultiValueMap<String, String>> request = composeSellRequest(
                coinName,
                currency,
                amountCoinToSell,
                currentPrice
        );
        final TradeResponseModel tradeResponseModel = restTemplate.postForObject(
                privateApi,
                request,
                TradeResponseModel.class
        );
        if (tradeResponseModel.getSuccess().equals(1)) {
            return;
        }
        throw new RuntimeException("Error On Selling Coin !!");
    }

    private HttpEntity<MultiValueMap<String, String>> composeSellRequest(String coinName, String currency,
                                                                         Double amountCoinToSell, Double currentPrice) {
        final Long nowInEpoch = getCurrentDateTimeInEpochMilis();
        final Long expiredTimeInEpoch = nowInEpoch + 5000L;

        MultiValueMap<String, String> map = composeSellBodyMap(coinName, currency, amountCoinToSell, currentPrice,
                nowInEpoch, expiredTimeInEpoch
        );
        return composeQueryParamWithCrendetial(map);
    }

    private HttpEntity<MultiValueMap<String, String>> composeQueryParamWithCrendetial(MultiValueMap<String, String> map) {
        String queryParam = QueryUtil.getQueryFrom(map);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Key", indodaxKey);
        httpHeaders.set("Sign", HmacUtil.from(queryParam, indodaxSecret));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(map, httpHeaders);
    }

    private MultiValueMap<String, String> composeSellBodyMap(String coinName, String currency,
                                                             Double amountCoinToSell, Double currentPrice,
                                                             Long nowInEpoch, Long expiredTimeInEpoch) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("type", "sell");
        map.add("method", "trade");
        map.add("price", currentPrice.toString());
        map.add("timestamp", nowInEpoch.toString());
        map.add("btc", amountCoinToSell.toString());
        map.add("recvWindow", expiredTimeInEpoch.toString());
        map.add("pair", coinName.toLowerCase() + "_" + currency.toLowerCase());
        return map;
    }

    @Override
    public List<TradeEvent> getPreviousTrade(String coinName, String currency) {
        final Long nowInEpoch = getCurrentDateTimeInEpochMilis();
        final Long expiredTimeInEpoch = nowInEpoch + 5000L;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("count", "5");
        map.add("method", "tradeHistory");
        map.add("timestamp", nowInEpoch.toString());
        map.add("recvWindow", expiredTimeInEpoch.toString());
        map.add("pair", coinName.toLowerCase() + "_" + currency.toLowerCase());

        final HttpEntity<MultiValueMap<String, String>> queryParam = composeQueryParamWithCrendetial(map);

        final HistoryOrderResponseModel historyOrderResponse = restTemplate.postForObject(
                privateApi,
                queryParam,
                HistoryOrderResponseModel.class
        );

        if (historyOrderResponse != null) {
            return historyOrderResponse.getMReturn()
                                       .getTrades()
                                       .stream()
                                       .map(eachTradeEvent -> TradeEvent.builder()
                                                                        .event(EventEnum.from(eachTradeEvent.getType()))
                                                                        .eventTime(convertEpochToLocalDateTime(eachTradeEvent))
                                                                        .eventPriceMoney(Optional.ofNullable(from(eachTradeEvent.getPrice())))
                                                                        .eventPriceBitcoin(Optional.ofNullable(from(eachTradeEvent.getEth())))
                                                                        .build())
                                       .collect(Collectors.toList());
        }
        throw new RuntimeException("history order empty !");
    }

    private LocalDateTime convertEpochToLocalDateTime(HistoryOrderReturnTradeModel latestBuy) {
        return LocalDateTime.ofEpochSecond(
                Long.valueOf(latestBuy.getTradeTime()),
                0,
                zone.getRules().getOffset(LocalDateTime.now())
        );
    }

    @Override
    public AssetInfo getAssetInfo() {
        final Long nowInEpoch = getCurrentDateTimeInEpochMilis();
        final Long expiredTimeInEpoch = nowInEpoch + 5000L;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("method", "getInfo");
        map.add("timestamp", nowInEpoch.toString());
        map.add("recvWindow", expiredTimeInEpoch.toString());
        final HttpEntity<MultiValueMap<String, String>> queryParam = composeQueryParamWithCrendetial(map);
        final AssetInfoResponseModel assetInfoResponseModel = restTemplate.postForObject(
                privateApi,
                queryParam,
                AssetInfoResponseModel.class
        );

        BitcoinBalanceInfo bitcoinBalanceInfo = new BitcoinBalanceInfo(
                "eth",
                assetInfoResponseModel.getMReturn().getBalance().getBtc()
        );
        return new AssetInfo(
                assetInfoResponseModel.getMReturn().getBalance().getIdr(),
                Collections.singletonList(bitcoinBalanceInfo)
        );
    }

    private Long getCurrentDateTimeInEpochMilis() {
        return LocalDateTime.now()
                            .atZone(zone)
                            .toInstant()
                            .toEpochMilli();
    }

}
