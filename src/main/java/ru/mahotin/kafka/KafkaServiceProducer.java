package ru.mahotin.kafka;

import ru.mahotin.web.dto.StatusTaskDTO;

public interface KafkaServiceProducer {
    void sendMessage(String topic, StatusTaskDTO msg);
    void sendMessageWithCallBack(String topic, StatusTaskDTO msg);
}
