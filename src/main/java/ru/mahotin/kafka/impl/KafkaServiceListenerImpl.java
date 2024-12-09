package ru.mahotin.kafka.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mahotin.kafka.KafkaServiceListener;

@Service
@Slf4j
public class KafkaServiceListenerImpl implements KafkaServiceListener {


    @KafkaListener(groupId = "group-1", topics = "status-topic")
    @Override
    public void listenGroup1(String message) {
        log.info("Message = {}", message);
    }
}
