package id.noxymon.miner.crawler.services.predictor.enums;

public enum HoursUnitPredictor implements TimeUnitPredictor {

    ONE_HOUR(1,"5 hour", 3, 32),
    THREE_HOUR(3,"10 hour", 3, 32),
    SIX_HOUR(6,"15 hour",3,32),
    TWELVE_HOUR(12,"15 hour",3,32),
    TWENTY_FOUR_HOUR(24,"15 hour",3,32);

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
