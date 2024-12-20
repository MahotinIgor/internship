package ru.mahotin.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import ru.mahotin.web.dto.StatusTaskDTO;
@Slf4j
public class CustomMessageSerializer implements Serializer<StatusTaskDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(String s, StatusTaskDTO dto) {
        try {
            if (dto == null){
                log.error("StatusTaskDTO = Null, send to Kafka empty byte[]");
                return new byte[0];
            }
            log.info("Serializing StatusTaskDTO to byte[]...");
            return objectMapper.writeValueAsBytes(dto);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageDto to byte[]");
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
