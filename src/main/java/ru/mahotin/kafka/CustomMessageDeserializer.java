package ru.mahotin.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import ru.mahotin.web.dto.StatusTaskDTO;

import java.util.Map;
@Slf4j

public class CustomMessageDeserializer implements Deserializer<StatusTaskDTO> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public StatusTaskDTO deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                log.error("Null received at deserializing to StatusTaskDTO");
                return new StatusTaskDTO(-1, "");
            }
            log.info("Deserializing byte[] to StatusTaskDTO");
            return objectMapper.readValue(
                    new String(data, "UTF-8"),
                    StatusTaskDTO.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to StatusTaskDTO");
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
