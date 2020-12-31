package id.noxymon.miner.crawler.services.predictor.enums;

public enum DaysUnitPredictor implements TimeUnitPredictor {

    THREE_DAYS(3,"3 hour", 72, 12),
    SEVEN_DAYS(7,"7 hour", 72, 12),
    FOURTEEN_DAYS(14,"14 hour", 72, 12),
    THIRTY_DAYS(30,"30 hour", 72, 12);

    private final Integer unitTime;
    private final String description;
    private final Integer maxLagQuery;
    private final Integer lagMakerMaxLag;

    DaysUnitPredictor(Integer unitTime, String description, Integer lagMakerMaxLag, Integer maxLagQuery) {
        this.unitTime = unitTime;
        this.description = description;
        this.lagMakerMaxLag = lagMakerMaxLag;
        this.maxLagQuery = maxLagQuery;
    }

    public Integer getUnitTime() {
        return unitTime;
    }

    public Integer getLagMakerMaxLag() {
        return lagMakerMaxLag;
    }

    public Integer getMaxHistoricalDataInterval() {
        return maxLagQuery;
    }
}
