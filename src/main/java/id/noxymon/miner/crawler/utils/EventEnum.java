package id.noxymon.miner.crawler.utils;

public enum EventEnum {
    BUY_COIN("Buy Coin", "Buy coin event"),
    SELL_COIN("Sell Coin", "Sell coin event");

    EventEnum(String eventName, String eventDescription) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
    }

    public static EventEnum from(String event){
        if("buy".equals(event)){
            return BUY_COIN;
        }

        if("sell".equals(event)){
            return SELL_COIN;
        }

        throw new RuntimeException(event + " name, unknown!");
    }

    private final String eventName;
    private final String eventDescription;
}
