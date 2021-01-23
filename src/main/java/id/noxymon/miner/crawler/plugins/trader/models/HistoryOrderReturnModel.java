package id.noxymon.miner.crawler.plugins.trader.models;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public class HistoryOrderReturnModel {
    private List<HistoryOrderReturnTradeModel> trades = Collections.emptyList();
}
