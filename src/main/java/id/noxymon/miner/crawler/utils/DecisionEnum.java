package id.noxymon.miner.crawler.utils;

public enum DecisionEnum {
    BUY(0, "Buy"),
    SELL(1, "Buy"),
    HOLD(2, "Buy");
    private final double value;
    private final String description;

    DecisionEnum(double value, String description) {
        this.value = value;
        this.description = description;
    }

    public static DecisionEnum from(double value){
        for (DecisionEnum decisionEnum : values()) {
            if(value == decisionEnum.value){
                return decisionEnum;
            }
        }
        throw new RuntimeException("No matching value decision !");
    }
}
