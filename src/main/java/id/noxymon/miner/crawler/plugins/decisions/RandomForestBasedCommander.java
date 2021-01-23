package id.noxymon.miner.crawler.plugins.decisions;

import id.noxymon.miner.crawler.repository.EtheriumMinutesRepository;
import id.noxymon.miner.crawler.repository.EtheriumPredictionRepository;
import id.noxymon.miner.crawler.repository.entities.TbEth;
import id.noxymon.miner.crawler.repository.entities.TbEthPrediction;
import id.noxymon.miner.crawler.services.decisions.BuyOrSellCommander;
import id.noxymon.miner.crawler.utils.DecisionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import weka.classifiers.trees.RandomForest;
import weka.core.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class RandomForestBasedCommander implements BuyOrSellCommander {

    private final EtheriumMinutesRepository etheriumMinutesRepository;
    private final EtheriumPredictionRepository etheriumPredictionRepository;

    private static final Attribute lastMinutePrice = new Attribute("real");
    private static final Attribute last60MinutePrice = new Attribute("1 hour");
    private static final Attribute last5MinutePrice = new Attribute("5 minutes");
    private static final Attribute last10MinutePrice = new Attribute("10 minutes");
    private static final Attribute last15MinutePrice = new Attribute("15 minutes");
    private static final Attribute comparison = new Attribute("comparison", new ArrayList<>(
            Arrays.asList("buy", "sell")));

    private static final ArrayList<Attribute> attributeList = new ArrayList<>(Arrays.asList(
            lastMinutePrice, last5MinutePrice, last10MinutePrice,
            last15MinutePrice, last60MinutePrice, comparison
    ));

    private static final String CLASSIFIER_FILE_NAME = "/home/noxymon/classifier_randomForest.model";

    @Override
    public void decideBuyOrSell() throws Exception {
        final DecisionEnum decision = makeDecision();

        if(decision.equals(DecisionEnum.BUY)){
            final TbEth lastMinutePrice = etheriumMinutesRepository.findLastMinutePrice();
        }
    }

    private DecisionEnum makeDecision() throws Exception {
        final Timestamp nowInTimestamp = Timestamp.valueOf(LocalDateTime.now());
        final TbEth lastMinutePrice = etheriumMinutesRepository.findLastMinutePrice();
        final TbEthPrediction last5MinutesPrediction = etheriumPredictionRepository.findPredictionFromLast5Minutes(nowInTimestamp);
        final TbEthPrediction last10MinutesPrediction = etheriumPredictionRepository.findPredictionFromLast10Minutes(nowInTimestamp);
        final TbEthPrediction last15MinutesPrediction = etheriumPredictionRepository.findPredictionFromLast15Minutes(nowInTimestamp);
        final TbEthPrediction last60MinutesPrediction = etheriumPredictionRepository.findPredictionFromLast60Minutes(nowInTimestamp);

        Instances header = new Instances("decision", attributeList, 0);
        header.setClassIndex(5);

        Instance instance = new SparseInstance(header.numAttributes());
        instance.setDataset(header);

        instance.setValue(attributeList.get(0), lastMinutePrice.getTbeClose().doubleValue());
        instance.setValue(attributeList.get(1), last5MinutesPrediction.getPredicted5Minutes());
        instance.setValue(attributeList.get(2), last10MinutesPrediction.getPredicted10Minutes());
        instance.setValue(attributeList.get(3), last15MinutesPrediction.getPredicted15Minutes());
        instance.setValue(attributeList.get(4), last60MinutesPrediction.getPredicted1Hour());

        final RandomForest randomForest = (RandomForest) SerializationHelper.read(CLASSIFIER_FILE_NAME);
        final double predicted = randomForest.classifyInstance(instance);
        return DecisionEnum.from(predicted);
    }
}
