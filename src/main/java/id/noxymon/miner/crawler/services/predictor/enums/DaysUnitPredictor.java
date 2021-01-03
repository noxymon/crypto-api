package id.noxymon.miner.crawler.services.predictor.enums;

public enum DaysUnitPredictor implements TimeUnitPredictor {

    THREE_DAYS(3,"3 hour", 72, 12, 4320),
    SEVEN_DAYS(7,"7 hour", 72, 12, 10080),
    FOURTEEN_DAYS(14,"14 hour", 72, 12, 20160),
    THIRTY_DAYS(30,"30 hour", 72, 12, 43200);

    private final Integer unitTime;
    private final String description;
    private final Integer maxLagQuery;
    private final Integer lagMakerMaxLag;
    private final Integer correctionTimeInMinutes;

    DaysUnitPredictor(Integer unitTime, String description, Integer maxLagQuery, Integer lagMakerMaxLag,
                      Integer correctionTimeInMinutes) {
        this.unitTime = unitTime;
        this.description = description;
        this.maxLagQuery = maxLagQuery;
        this.lagMakerMaxLag = lagMakerMaxLag;
        this.correctionTimeInMinutes = correctionTimeInMinutes;
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

    @Override
    public Integer getCorrectionTime() {
        return correctionTimeInMinutes;
    }


}
