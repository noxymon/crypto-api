package id.noxymon.miner.crawler.services.predictor.enums;

public interface TimeUnitPredictor {
    Integer getUnitTime();
    Integer getLagMakerMaxLag();
    Integer getMaxHistoricalDataInterval();
    Integer getCorrectionTime();
}
