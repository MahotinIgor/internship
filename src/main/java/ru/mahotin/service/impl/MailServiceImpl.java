package ru.mahotin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.mahotin.service.MailService;
import ru.mahotin.service.props.MailProperties;

@RequiredArgsConstructor
@Component
public class MailServiceImpl implements MailService {

        private final JavaMailSender emailSender;
        private final MailProperties props;
        public void sendSimpleMessage(
                String to, String subject, String text) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(props.getUsername());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        }
}
