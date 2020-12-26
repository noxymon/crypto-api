package id.noxymon.miner.crawler.plugins.fetcher.indodax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.noxymon.miner.crawler.plugins.fetcher.indodax.models.IndodaxApiResponse;
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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndodaxApiFetcher implements FetcherData {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final EtheriumMinutesRepository etheriumMinutesRepository;

    @Value("${application.data.historical}")
    private String minutesDataApi;

    private ZoneId zone = ZoneId.of("Asia/Jakarta");

    @Override
    public void fetchData(String cryptoCurrency, LocalDateTime target) {
        final LocalDateTime targetTimeHour = target.truncatedTo(ChronoUnit.MINUTES);
        final LocalDateTime startTime = targetTimeHour.minusHours(3);
        fetchData(cryptoCurrency, startTime, targetTimeHour);
    }

    public void fetchData(String cryptoCurrency, LocalDateTime startTime, LocalDateTime targetTimeHour) {
        try {
            collectEtheriumPriceInMinutesInterval(cryptoCurrency, targetTimeHour, startTime);
        } catch (InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void collectEtheriumPriceInMinutesInterval(String cryptoCurrency, LocalDateTime targetTimeHour, LocalDateTime startTime) throws InterruptedException, JsonProcessingException {
        if(rangeDateExceed(targetTimeHour, startTime)){
            fetchDateFromApi(cryptoCurrency, startTime, targetTimeHour);
        }else{
            collectEtheriumPriceInMinutesIntervalChunked(cryptoCurrency, targetTimeHour, startTime);
        }
    }

    private void collectEtheriumPriceInMinutesIntervalChunked(String cryptoCurrency, LocalDateTime targetTimeHour, LocalDateTime startTime) throws JsonProcessingException, InterruptedException {
        LocalDateTime justifiedStartTime = targetTimeHour.minusMonths(1);
        LocalDateTime justifiedEndTime = targetTimeHour;
        while(startTime.isBefore(justifiedStartTime) || startTime.isEqual(justifiedStartTime)){
            fetchDateFromApi(cryptoCurrency, justifiedStartTime, justifiedEndTime);
            justifiedEndTime = justifiedStartTime;
            justifiedStartTime = justifiedStartTime.minusHours(1);
            Thread.sleep(5000);
        }
    }

    private boolean rangeDateExceed(LocalDateTime targetTimeHour, LocalDateTime startTime) {
        return ChronoUnit.MONTHS.between(startTime, targetTimeHour) <= 1;
    }

    private List<TbEth> fetchDateFromApi(String cryptoCurrency, LocalDateTime startTime, LocalDateTime targetTimeHour) throws JsonProcessingException {
        final URI dataHistoricalApiUrl = getDataHistoricalApiUrl(cryptoCurrency, startTime, targetTimeHour);
        final String responseApiString = restTemplate.getForObject(dataHistoricalApiUrl, String.class);
        final IndodaxApiResponse indodaxApiResponse = objectMapper.readValue(responseApiString, IndodaxApiResponse.class);

        checkIfApiReturnOK(indodaxApiResponse);

        int indexItem = 0;
        List<TbEth> etheriumPriceInMinuteList = new ArrayList<>();
        for (Long timestampInEpochOfSecond : indodaxApiResponse.getTimestamp()) {
            final double lowPrice = indodaxApiResponse.getLowPrice().get(indexItem);
            final double openPrice = indodaxApiResponse.getOpenPrice().get(indexItem);
            final double highPrice = indodaxApiResponse.getHighPrice().get(indexItem);
            final double closePrice = indodaxApiResponse.getClosePrice().get(indexItem);
            final LocalDateTime timestampInLocalDateTime = LocalDateTime.ofEpochSecond(
                    timestampInEpochOfSecond,
                    0,
                    zone.getRules().getOffset(LocalDateTime.now())
            );

            TbEth etheriumPriceInMinutes = new TbEth();
            etheriumPriceInMinutes.setTbeDate(Timestamp.valueOf(timestampInLocalDateTime));
            etheriumPriceInMinutes.setTbeOpen(BigDecimal.valueOf(openPrice));
            etheriumPriceInMinutes.setTbeHigh(BigDecimal.valueOf(highPrice));
            etheriumPriceInMinutes.setTbeLow(BigDecimal.valueOf(lowPrice));
            etheriumPriceInMinutes.setTbeClose(BigDecimal.valueOf(closePrice));

            etheriumPriceInMinuteList.add(etheriumPriceInMinutes);
            log.info(timestampInLocalDateTime + " : open_price=" + openPrice + ", high_price=" + highPrice + ", low_price="+lowPrice + ", close_price="+closePrice);
            indexItem++;
        }
        etheriumMinutesRepository.saveAll(etheriumPriceInMinuteList);
        return etheriumPriceInMinuteList;
    }

    private void checkIfApiReturnOK(IndodaxApiResponse indodaxApiResponse) {
        if(!"ok".equals(indodaxApiResponse.getStatus())){
            throw new RuntimeException("Error API Response indodax");
        }
    }

    private URI getDataHistoricalApiUrl(String cryptoCurrency, LocalDateTime startTime, LocalDateTime expectedEndtime) {
        return UriComponentsBuilder.fromHttpUrl(minutesDataApi)
                .queryParam("symbol", cryptoCurrency)
                .queryParam("resolution", 1)
                .queryParam("from", startTime.toEpochSecond(zone.getRules().getOffset(LocalDateTime.now())))
                .queryParam("to", expectedEndtime.toEpochSecond(zone.getRules().getOffset(LocalDateTime.now())))
                .build()
                .toUri();
    }
}
