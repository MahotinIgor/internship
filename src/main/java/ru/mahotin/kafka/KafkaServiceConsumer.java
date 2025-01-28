package ru.mahotin.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import ru.mahotin.web.dto.StatusTaskDTO;

public interface KafkaServiceConsumer {
    void listenGroup1(ConsumerRecord<String, StatusTaskDTO[]> record, Acknowledgment ack);
}
