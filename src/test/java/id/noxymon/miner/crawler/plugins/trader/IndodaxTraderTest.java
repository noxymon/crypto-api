package id.noxymon.miner.crawler.plugins.trader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import id.noxymon.miner.crawler.services.trader.models.AssetInfo;
import id.noxymon.miner.crawler.services.trader.models.TradeEvent;

@SpringBootTest
@TestPropertySource(properties = {
        "application.prediction.enable=false"})
class IndodaxTraderTest {
    @Autowired
    private IndodaxTrader mIndodaxTrader;

    @Test
    public void testAssetInfo() {
        final AssetInfo assetInfo = mIndodaxTrader.getAssetInfo();
        Assertions.assertNotNull(assetInfo);
    }

    @Test
    public void testPreviousTrade() {
        final List<TradeEvent> previousTrade = mIndodaxTrader.getPreviousTrade("ETH", "IDR");
        Assertions.assertNotNull(previousTrade);
    }
}