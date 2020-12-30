package id.noxymon.miner.crawler.services.predictor.enums;

public enum MinutesUnitPredictor implements TimeUnitPredictor {

    FIVE_MINUTES(5,"5 minnute",3,32),
    TEN_MINUTES(10,"10 minute",3,32),
    FIFTEEN_MINUTES(15,"15 minute",3,32);

    private final Integer unitTime;
    private final String description;
    private final Integer maxLagQuery;
    private final Integer lagMakerMaxLag;

    MinutesUnitPredictor(Integer unitTime, String description, Integer maxLagQuery, Integer lagMakerMaxLag) {
        this.unitTime = unitTime;
        this.description = description;
        this.maxLagQuery = maxLagQuery;
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
    public Integer getMaxLagQuery() {
        return maxLagQuery;
    }
}
