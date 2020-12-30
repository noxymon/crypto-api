package id.noxymon.miner.crawler.services.predictor.enums;

public enum HoursUnitPredictor implements TimeUnitPredictor {

    ONE_HOUR(1,"5 hour", 72, 3),
    THREE_HOUR(3,"10 hour", 72, 3),
    SIX_HOUR(6,"15 hour",72,3),
    TWELVE_HOUR(12,"15 hour",72,3),
    TWENTY_FOUR_HOUR(24,"15 hour",72,3);

    private final Integer unitTime;
    private final String description;
    private final Integer maxHistoricalDataInterval;
    private final Integer lagMakerMaxLag;

    HoursUnitPredictor(Integer unitTime, String description, Integer maxHistoricalDataInterval, Integer lagMakerMaxLag) {
        this.unitTime = unitTime;
        this.description = description;
        this.maxHistoricalDataInterval = maxHistoricalDataInterval;
        this.lagMakerMaxLag = lagMakerMaxLag;
    }

    public Integer getUnitTime() {
        return unitTime;
    }

    @Override
    public Integer getLagMakerMaxLag() {
        return lagMakerMaxLag;
    }

    @Override
    public Integer getMaxHistoricalDataInterval() {
        return maxHistoricalDataInterval;
    }
}
