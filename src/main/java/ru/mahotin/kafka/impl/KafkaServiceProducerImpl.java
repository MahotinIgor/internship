package ru.mahotin.kafka.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.mahotin.kafka.KafkaServiceProducer;
import ru.mahotin.web.dto.StatusTaskDTO;

import java.util.concurrent.CompletableFuture;

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
                System.out.println("Sent message=[" + msg +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        msg + "] due to : " + ex.getMessage());
            }
        });
    }
}
