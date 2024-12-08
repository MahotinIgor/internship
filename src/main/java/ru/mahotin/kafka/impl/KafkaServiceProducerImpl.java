package ru.mahotin.kafka.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.mahotin.kafka.KafkaServiceProducer;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class KafkaServiceProducerImpl implements KafkaServiceProducer {

    private  final KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void sendMessage(String topic, String msg) {
        kafkaTemplate.send(topic, msg);
    }

    @Override
    public void sendMessageWithCallBack(String topic, String msg) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, msg);
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
