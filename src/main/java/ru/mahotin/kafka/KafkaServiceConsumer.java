package ru.mahotin.kafka;

import ru.mahotin.web.dto.StatusTaskDTO;

public interface KafkaServiceConsumer {
    void listenGroup1(StatusTaskDTO message);
}
