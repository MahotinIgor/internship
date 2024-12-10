package ru.mahotin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.mahotin.service.MailService;

@RequiredArgsConstructor
@Component
public class MailServiceImpl implements MailService {

        private final JavaMailSender emailSender;
        @Value("${spring.mail.username}")
        private String userMail;

        public void sendSimpleMessage(
                String to, String subject, String text) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(userMail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);

        }
}
