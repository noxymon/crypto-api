package id.noxymon.miner.crawler.plugins.fetcher.summarizer;

import id.noxymon.miner.crawler.repository.EtheriumHourlyRepository;
import id.noxymon.miner.crawler.repository.EtheriumMinutesRepository;
import id.noxymon.miner.crawler.repository.entities.TbEth;
import id.noxymon.miner.crawler.repository.entities.TbEthHour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HourSummarizer {

    private final EtheriumHourlyRepository etheriumHourlyRepository;
    private final EtheriumMinutesRepository etheriumMinutesRepository;

    public void summarize(LocalDateTime startTime, LocalDateTime endTime){
        final List<TbEth> tbEths = etheriumMinutesRepository.summarizeHourly(Timestamp.valueOf(startTime), Timestamp.valueOf(endTime));
        for (TbEth summarizedEtherium : tbEths) {
            TbEthHour etheriumHour = new TbEthHour();
            etheriumHour.setTbDate(summarizedEtherium.getTbeDate());
            etheriumHour.setOpenAvg(summarizedEtherium.getTbeOpen().doubleValue());
            etheriumHour.setCloseAvg(summarizedEtherium.getTbeClose().doubleValue());
            etheriumHour.setHighAvg(summarizedEtherium.getTbeHigh().doubleValue());
            etheriumHour.setLowAvg(summarizedEtherium.getTbeLow().doubleValue());

            etheriumHourlyRepository.save(etheriumHour);
            log.info(
                    summarizedEtherium.getTbeDate() +
                    " : open_price=" + summarizedEtherium.getTbeOpen().doubleValue() +
                    ", high_price=" + summarizedEtherium.getTbeHigh().doubleValue() +
                    ", low_price="+summarizedEtherium.getTbeLow().doubleValue() +
                    ", close_price="+summarizedEtherium.getTbeClose().doubleValue());
        }
    }
}
