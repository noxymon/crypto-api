package id.noxymon.miner.crawler.services.messenger;

public interface MessageSender {
    void sendMessage(String message, String targetId);
}
