package id.noxymon.miner.crawler.services.trader;

import java.util.List;

import id.noxymon.miner.crawler.services.trader.models.AssetInfo;
import id.noxymon.miner.crawler.services.trader.models.TradeEvent;

public interface Trader {
    void buyCoin(String coinName, String currency, Double amountMoneyToBuy, Double currentPrice);
    void sellCoin(String coinName, String currency, Double amountCoinToSell, Double currentPrice);
    List<TradeEvent> getPreviousTrade(String coinName, String currency);
    AssetInfo getAssetInfo();
}
