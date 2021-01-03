package id.noxymon.miner.crawler.services.predictor.enums;

public enum HoursUnitPredictor implements TimeUnitPredictor {

    ONE_HOUR(1,"1 hour", 72, 3, 60),
    THREE_HOUR(3,"10 hour", 72, 3, 180),
    SIX_HOUR(6,"15 hour",72,3, 360),
    TWELVE_HOUR(12,"15 hour",72,3, 720),
    TWENTY_FOUR_HOUR(24,"15 hour",72,3, 1440);

    private final Integer unitTime;
    private final String description;
    private final Integer maxHistoricalDataInterval;
    private final Integer lagMakerMaxLag;
    private final Integer correctionTimeInMinutes;

    HoursUnitPredictor(Integer unitTime, String description, Integer maxHistoricalDataInterval, Integer lagMakerMaxLag,
                       Integer correctionTimeInMinutes) {
        this.unitTime = unitTime;
        this.description = description;
        this.maxHistoricalDataInterval = maxHistoricalDataInterval;
        this.lagMakerMaxLag = lagMakerMaxLag;
        this.correctionTimeInMinutes = correctionTimeInMinutes;
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

    @Override
    public Integer getCorrectionTime() {
        return correctionTimeInMinutes;
    }


}
