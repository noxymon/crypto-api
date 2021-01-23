package id.noxymon.miner.crawler.plugins.decisions;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import id.noxymon.miner.crawler.repository.EtheriumMinutesRepository;
import id.noxymon.miner.crawler.repository.EtheriumPredictionRepository;
import id.noxymon.miner.crawler.repository.entities.TbEth;
import id.noxymon.miner.crawler.repository.entities.TbEthPrediction;
import id.noxymon.miner.crawler.services.decisions.BuyOrSellCommander;
import id.noxymon.miner.crawler.services.messenger.MessageSender;
import id.noxymon.miner.crawler.services.trader.Trader;
import id.noxymon.miner.crawler.services.trader.models.AssetInfo;
import id.noxymon.miner.crawler.services.trader.models.TradeEvent;
import id.noxymon.miner.crawler.utils.EventEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleCommander implements BuyOrSellCommander {

    private final Trader mTrader;
    private final MessageSender mMessageSender;
    private final EtheriumMinutesRepository etheriumMinutesRepository;

    @Override
    public void decideBuyOrSell() throws Exception {
        final AssetInfo assetInfo = mTrader.getAssetInfo();
        final TbEth lastMinutePrice = etheriumMinutesRepository.findLastMinutePrice();
        final List<TradeEvent> previousTrade = mTrader.getPreviousTrade("ETH", "IDR");

        if(assetInfo.getMoneyBalance() < 1000){
            sellCoin(assetInfo, previousTrade, lastMinutePrice);
        }

        if(assetInfo.getMoneyBalance() > 50000){
            final Optional<TradeEvent> lastBuyEvent = previousTrade.stream()
                                                            .filter(tradeEvent -> tradeEvent.getEvent()
                                                                                            .equals(EventEnum.BUY_COIN))
                                                            .findFirst();
            if(lastBuyEvent.isPresent()){
                final double currentPrice = lastMinutePrice.getTbeClose().doubleValue();
                final double estimateCurrentBitCoin = assetInfo.getMoneyBalance() / currentPrice;
                if(estimateCurrentBitCoin > lastBuyEvent.get().getEventPriceBitcoin().get()){
                    mMessageSender.sendMessage(sendMessageOf(currentPrice, estimateCurrentBitCoin), "noxymon");
                    mTrader.buyCoin("ETH", "IDR", assetInfo.getMoneyBalance(), currentPrice);
                }
            }
        }
    }

    private String sendMessageOf(double currentPrice, double estimateCurrentBitCoin) {
        return "Buy At " + currentPrice + " estimate coin " + estimateCurrentBitCoin;
    }

    private void sellCoin(AssetInfo assetInfo, List<TradeEvent> previousTrade, TbEth lastMinutePrice) {
        final Optional<TradeEvent> lastBuyTransaction = findLastBuyTransaction(previousTrade);
        if(lastBuyTransaction.isPresent()){
            final TradeEvent tradeEvent = lastBuyTransaction.get();
            final double lastprice = lastMinutePrice.getTbeClose().doubleValue() + 5000;
            if(lastprice > tradeEvent.getEventPriceMoney().get()){
                mMessageSender.sendMessage(sendMessageOf(assetInfo, lastprice), "noxymon");
                mTrader.sellCoin(
                        "ETH",
                        "IDR",
                        assetInfo.getBitCoinList().get(0).getBitCoinBalance(),
                        lastMinutePrice.getTbeClose().doubleValue()
                );
            }
        }
    }

    private String sendMessageOf(AssetInfo assetInfo, double lastprice) {
        return "Sell " + assetInfo.getBitCoinList().get(0).getBitCoinBalance() + " At " + lastprice;
    }

    private Optional<TradeEvent> findLastBuyTransaction(List<TradeEvent> previousTrade) {
        return previousTrade.stream()
                            .filter(eachTrade -> eachTrade.getEvent().equals(EventEnum.BUY_COIN))
                            .findFirst();
    }

    private EventEnum getLastEvent(List<TradeEvent> previousTrade) {
        return previousTrade.get(0).getEvent();
    }
}
