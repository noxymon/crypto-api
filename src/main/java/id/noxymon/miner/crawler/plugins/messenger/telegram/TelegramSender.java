package id.noxymon.miner.crawler.plugins.messenger.telegram;

import id.noxymon.miner.crawler.plugins.messenger.telegram.models.ApiResponse;
import id.noxymon.miner.crawler.plugins.messenger.telegram.models.Message;
import id.noxymon.miner.crawler.plugins.messenger.telegram.models.SingleApiResponse;
import id.noxymon.miner.crawler.services.messenger.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramSender implements MessageSender {

    @Value("${application.messenger.telegram-bot-api}")
    private String telegramBotApi;
    @Value("${application.messenger.telegram-token}")
    private String telegramBotToken;

    private final RestTemplate restTemplate;

    @Override
    public void sendMessage(String message, String targetId) {
        Integer chatIdFromTarget = getChatApiFromUsername(targetId);
        sendMessageToApi(message, chatIdFromTarget);
    }

    private Integer getChatApiFromUsername(String username){
        final String getUpdateUrl = telegramBotApi + "/" + "bot" + telegramBotToken + "/getUpdates";
        final ApiResponse apiResponse = restTemplate.getForObject(getUpdateUrl, ApiResponse.class);
        final Optional<Message> messageFilteredByUsername = apiResponse.getResult()
                .stream()
                .filter(message -> username.equals(message.getMessageDetail().getFrom().getUsername()))
                .findFirst();
        if(messageFilteredByUsername.isPresent()){
            return messageFilteredByUsername.get().getMessageDetail().getChat().getId();
        }
        throw new RuntimeException("No Conversation yet with " + username);
    }

    private void sendMessageToApi(String message, Integer chatId){
        String sendMessageUrl = telegramBotApi + "/" + "bot" + telegramBotToken +
                "/sendMessage?chat_id="+chatId+"&text="+message;
        final SingleApiResponse apiResponse = restTemplate.getForObject(sendMessageUrl, SingleApiResponse.class);
        if(!apiResponse.getOk()){
            throw new RuntimeException("Failed Send Message");
        }
    }
}
