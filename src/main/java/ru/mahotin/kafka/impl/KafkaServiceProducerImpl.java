package ru.mahotin.kafka.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.mahotin.kafka.KafkaServiceProducer;
import ru.mahotin.web.dto.StatusTaskDTO;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaServiceProducerImpl implements KafkaServiceProducer {

    private  final KafkaTemplate<String, StatusTaskDTO> kafkaTemplate;
    @Override
    public void sendMessage(String topic, StatusTaskDTO msg) {
        kafkaTemplate.send(topic, msg);
    }

    @Override
    public void sendMessageWithCallBack(String topic, StatusTaskDTO msg) {
        CompletableFuture<SendResult<String, StatusTaskDTO>> future = kafkaTemplate.send(topic, msg);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[" + msg +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                log.error("Unable to send message=[" +
                        msg + "] due to : " + ex.getMessage());
            }
        });
    }
}
