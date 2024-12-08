package ru.mahotin.kafka;

public interface KafkaServiceProducer {
    void sendMessage(String topic, String msg);
    void sendMessageWithCallBack(String topic, String msg);
}
