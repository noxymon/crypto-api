package id.noxymon.miner.crawler.plugins.fetcher.api;

import id.noxymon.miner.crawler.plugins.fetcher.api.models.ApiResponse;
import id.noxymon.miner.crawler.plugins.fetcher.summarizer.HourSummarizer;
import id.noxymon.miner.crawler.repository.EtheriumHourlyRepository;
import id.noxymon.miner.crawler.repository.EtheriumMinutesRepository;
import id.noxymon.miner.crawler.repository.entities.TbEth;
import id.noxymon.miner.crawler.services.fetcher.FetcherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinDeskApiFetcher implements FetcherData {

    private final RestTemplate restTemplate;
    private final HourSummarizer hourSummarizer;
    private final EtheriumHourlyRepository etheriumHourlyRepository;
    private final EtheriumMinutesRepository etheriumMinutesRepository;

    @Value("${application.data.historical}")
    private String minutesDataApi;

    @Override
    public void fetchData(String cryptoCurrency, LocalDateTime targetTime) {
        final LocalDateTime targetTimeHour = targetTime.truncatedTo(ChronoUnit.HOURS);
        final LocalDateTime startTime = targetTimeHour.minusHours(3);
        try {
            fetchDataOfBetween(cryptoCurrency, targetTimeHour, startTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void fetchDataOfBetween(String cryptoCurrency, LocalDateTime startTime, LocalDateTime targetTime) throws InterruptedException {
        List<TbEth> etheriumPriceInMinuteList = new ArrayList<>();
        if(ChronoUnit.HOURS.between(startTime, targetTime) <= 23){
            etheriumPriceInMinuteList.addAll(fetchFromApi(cryptoCurrency, startTime, targetTime));
        }else{
            LocalDateTime justifiedStartTime = targetTime.minusHours(1).truncatedTo(ChronoUnit.HOURS);
            LocalDateTime justifiedTarget = targetTime;
            while(startTime.isBefore(justifiedStartTime) || startTime.isEqual(justifiedStartTime)){
                etheriumPriceInMinuteList.addAll(fetchFromApi("ETH", justifiedStartTime, justifiedTarget));
                justifiedTarget = justifiedStartTime;
                justifiedStartTime = justifiedStartTime.minusHours(1);
                Thread.sleep(1000);
            }
        }

        etheriumMinutesRepository.saveAll(etheriumPriceInMinuteList);
        hourSummarizer.summarize(startTime, targetTime);
    }

    private List<TbEth> fetchFromApi(String cryptoCurrency, LocalDateTime startTime, LocalDateTime targetTimeHour) {
        final URI dataHistoricalApi = getDataHistoricalApiUrl(cryptoCurrency,
                startTime.truncatedTo(ChronoUnit.HOURS),
                targetTimeHour.truncatedTo(ChronoUnit.HOURS)
        );
        final ApiResponse apiResponse = restTemplate.getForObject(dataHistoricalApi, ApiResponse.class);

        if(!"1-minute".equals(apiResponse.getData().getInterval())){
            throw new RuntimeException("Not 1 Minutes !!");
        }

        List<TbEth> etheriumPriceInMinuteList= new ArrayList<>();
        for (List<Double> entry : apiResponse.getData().getEntries()) {
            final LocalDateTime dateTime = getLocalDateTimeFromApiEntry(entry);

            final double openPrice = entry.get(1);
            final double highPrice = entry.get(2);
            final double lowPrice = entry.get(3);
            final double closePrice = entry.get(4);

            TbEth etheriumPriceInMinutes = new TbEth();
            etheriumPriceInMinutes.setTbeDate(Timestamp.valueOf(dateTime));
            etheriumPriceInMinutes.setTbeOpen(BigDecimal.valueOf(openPrice));
            etheriumPriceInMinutes.setTbeHigh(BigDecimal.valueOf(highPrice));
            etheriumPriceInMinutes.setTbeLow(BigDecimal.valueOf(lowPrice));
            etheriumPriceInMinutes.setTbeClose(BigDecimal.valueOf(closePrice));

            etheriumPriceInMinuteList.add(etheriumPriceInMinutes);
            log.info(dateTime + " : open_price=" + openPrice + ", high_price=" + highPrice + ", low_price="+lowPrice + ", close_price="+closePrice);
        }
        return etheriumPriceInMinuteList;
    }

    private LocalDateTime getLocalDateTimeFromApiEntry(List<Double> entry) {
        final long dateTimeEntry = entry.get(0).longValue()/1000;
        return LocalDateTime.ofEpochSecond(dateTimeEntry, 0, ZoneOffset.UTC);
    }

    private URI getDataHistoricalApiUrl(String cryptoCurrency, LocalDateTime startTime, LocalDateTime expectedEndtime) {
        return UriComponentsBuilder.fromHttpUrl(minutesDataApi)
                .path("/" + cryptoCurrency)
                .queryParam("start_date", startTime)
                .queryParam("end_date", expectedEndtime)
                .queryParam("ohlc", true)
                .build()
                .toUri();
    }
}
