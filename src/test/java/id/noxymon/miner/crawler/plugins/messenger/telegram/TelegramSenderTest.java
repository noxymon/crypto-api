package id.noxymon.miner.crawler.plugins.messenger.telegram;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TelegramSenderTest {
    @Autowired
    private TelegramSender telegramSender;

    @Test
    public void testSend(){
        telegramSender.sendMessage("Ini Contoh test terkirim", "noxymon");
    }
}