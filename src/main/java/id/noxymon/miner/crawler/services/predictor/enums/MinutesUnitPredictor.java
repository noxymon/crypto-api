package id.noxymon.miner.crawler.services.predictor.enums;

public enum MinutesUnitPredictor implements TimeUnitPredictor {

    FIVE_MINUTES(5,"5 minnute",72,3, 6),
    TEN_MINUTES(10,"10 minute",72,3, 11),
    FIFTEEN_MINUTES(15,"15 minute",72,3, 16);

    private final Integer unitTime;
    private final String description;
    private final Integer maxLagQuery;
    private final Integer lagMakerMaxLag;
    private final Integer correctionTime;

    MinutesUnitPredictor(Integer unitTime, String description, Integer maxLagQuery, Integer lagMakerMaxLag,
                         Integer correctionTime) {
        this.unitTime = unitTime;
        this.description = description;
        this.maxLagQuery = maxLagQuery;
        this.lagMakerMaxLag = lagMakerMaxLag;
        this.correctionTime = correctionTime;
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
        return maxLagQuery;
    }

    @Override
    public Integer getCorrectionTime() {
        return correctionTime;
    }


}
