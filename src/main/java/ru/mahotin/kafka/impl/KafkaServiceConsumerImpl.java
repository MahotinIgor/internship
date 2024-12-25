package ru.mahotin.kafka.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.mahotin.kafka.KafkaServiceConsumer;
import ru.mahotin.service.MailService;
import ru.mahotin.service.props.MailProperties;
import ru.mahotin.web.dto.StatusTaskDTO;

@Service
@Slf4j
public class KafkaServiceConsumerImpl implements KafkaServiceConsumer {
    private final MailService mailService;
    private final MailProperties props;

    public KafkaServiceConsumerImpl(MailService mailService, MailProperties props) {
        this.mailService = mailService;
        this.props = props;
    }

    @KafkaListener(groupId = "group-1", topics = "status-topic")
    @Override
    public void listenGroup1(ConsumerRecord<String, StatusTaskDTO[]> record,
                             Acknowledgment ack
    ) {
        String subject;
        String text;
        StatusTaskDTO[] messages = record.value();
        for(StatusTaskDTO message : messages) {
            log.info("Received message from kafka = {}", message);
            if (message.taskid() < 0) {
                subject = "Error when update status task";
                text = "Null received at deserializing to StatusTaskDTO";
            } else {
                subject = String.format("Status task id = %d updated", message.taskid());
                text = String.format("Task id = %d, new status = %s",
                        message.taskid(),
                        message.newStatus()
                );
            }
            mailService.sendSimpleMessage(props.getUsername(), subject, text);
        }
        ack.acknowledge();

    }
}
