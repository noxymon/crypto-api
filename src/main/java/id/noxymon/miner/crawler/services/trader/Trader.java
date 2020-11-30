package id.noxymon.miner.crawler.services.trader;

public interface Trader {
    void buyCoin(String coinName, String currency, Double amountMoneyToBuy, Double currentPrice);
    void sellCoin(String coinName, String currency, Double amountCoinToSell, Double currentPrice);
}
