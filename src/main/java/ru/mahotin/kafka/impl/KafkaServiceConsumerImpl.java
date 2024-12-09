package ru.mahotin.kafka.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mahotin.kafka.KafkaServiceConsumer;
import ru.mahotin.web.dto.StatusTaskDTO;

@Service
@Slf4j
public class KafkaServiceConsumerImpl implements KafkaServiceConsumer {
    @KafkaListener(groupId = "group-1", topics = "status-topic")
    @Override
    public void listenGroup1(StatusTaskDTO message) {
        log.info("Received message from kafka = {}", message);
    }
}
