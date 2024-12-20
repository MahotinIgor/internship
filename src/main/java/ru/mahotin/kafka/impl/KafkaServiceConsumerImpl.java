package ru.mahotin.kafka.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mahotin.kafka.KafkaServiceConsumer;
import ru.mahotin.service.MailService;
import ru.mahotin.web.dto.StatusTaskDTO;

@Service
@Slf4j
public class KafkaServiceConsumerImpl implements KafkaServiceConsumer {
    private final MailService mailService;
    @Value("${spring.mail.username}")
    private String userName;

    public KafkaServiceConsumerImpl(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(groupId = "group-1", topics = "status-topic")
    @Override
    public void listenGroup1(StatusTaskDTO message) {
        String subject;
        String text;
        log.info("Received message from kafka = {}", message);

        if(message.taskid() < 0) {
            subject = "Error when update status task";
            text = "Null received at deserializing to StatusTaskDTO";
        } else {
            subject = String.format("Status task id = %d updated",message.taskid());
            text = String.format("Task id = %d, new status = %s",
                    message.taskid(),
                    message.newStatus()
            );
        }
        mailService.sendSimpleMessage(userName,subject,text);
    }
}
