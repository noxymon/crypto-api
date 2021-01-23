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
import id.noxymon.miner.crawler.services.trader.Trader;
import id.noxymon.miner.crawler.services.trader.models.AssetInfo;
import id.noxymon.miner.crawler.services.trader.models.TradeEvent;
import id.noxymon.miner.crawler.utils.EventEnum;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimpleCommander implements BuyOrSellCommander {

    private final Trader mTrader;
    private final EtheriumMinutesRepository etheriumMinutesRepository;
    private final EtheriumPredictionRepository mEtheriumPredictionRepository;

    @Override
    public void decideBuyOrSell() throws Exception {
        final AssetInfo assetInfo = mTrader.getAssetInfo();
        final TbEth lastMinutePrice = etheriumMinutesRepository.findLastMinutePrice();
        final List<TradeEvent> previousTrade = mTrader.getPreviousTrade("ETH", "IDR");

        if(assetInfo.getMoneyBalance() < 1000){
            sellCoin(assetInfo, previousTrade, lastMinutePrice);
        }

        if(assetInfo.getMoneyBalance() > 50000){
            buyCoin(assetInfo, lastMinutePrice);
        }
    }

    private void buyCoin(AssetInfo assetInfo, TbEth lastMinutePrice) {
        final TbEthPrediction predictionFromLast5Minutes = mEtheriumPredictionRepository
                .findPredictionFromLast5Minutes(Timestamp.valueOf(LocalDateTime.now()));
        if(Objects.nonNull(predictionFromLast5Minutes.getPredicted5Minutes())){
            if(predictionFromLast5Minutes.getPredicted5Minutes() >= lastMinutePrice.getTbeClose().doubleValue()){
                mTrader.buyCoin("ETH", "IDR", assetInfo.getMoneyBalance(), lastMinutePrice.getTbeClose().doubleValue());
            }
        }
    }

    private void sellCoin(AssetInfo assetInfo, List<TradeEvent> previousTrade, TbEth lastMinutePrice) {
        final Optional<TradeEvent> lastBuyTransaction = findLastBuyTransaction(previousTrade);
        if(lastBuyTransaction.isPresent()){
            final TradeEvent tradeEvent = lastBuyTransaction.get();
            if((lastMinutePrice.getTbeClose().doubleValue() + 5000) > tradeEvent.getEventPriceMoney().get()){
                mTrader.sellCoin(
                        "ETH",
                        "IDR",
                        assetInfo.getBitCoinList().get(0).getBitCoinBalance(),
                        lastMinutePrice.getTbeClose().doubleValue()
                );
            }
        }
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
