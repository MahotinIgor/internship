package ru.mahotin;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import ru.mahotin.entity.Status;
import ru.mahotin.entity.Task;
import ru.mahotin.repository.TaskRepository;
import ru.mahotin.service.impl.TaskServiceImpl;
import ru.mahotin.web.dto.TaskGetDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DemoApplicationTests  extends TestContainer{

    @Autowired
    TaskServiceImpl taskService;

    @Autowired
    TaskRepository taskRepository;



    @Test
    @DisplayName("Контекст успешно запускается")
    void contextLoads() {
    }

    @Test
    @DisplayName("Поиск всех Task")
    void getAll() {
        Task task1 = new Task();
        task1.setId(2L);
        task1.setTitle("mock status 1");
        task1.setDescription("mock description 1");
        task1.setStatus(Status.IN_PROGRESS);
        task1.setUserId(1L);

        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setId(3L);
        task2.setTitle("mock status 2");
        task2.setDescription("mock description 2");
        task2.setStatus(Status.NEW);
        task2.setUserId(2L);

        taskRepository.save(task2);



//        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));
//        when(taskMapper.dtoFromEntity(task1)).thenReturn(new TaskGetDTO(2L,
//                "mock status 1",
//                "mock description 1",
//                "in_progress",
//                1L));
//        when(taskMapper.dtoFromEntity(task2)).thenReturn(new TaskGetDTO(3L,
//                "mock status 2",
//                "mock description 2",
//                "new",
//                2L));

        List<TaskGetDTO> res = taskService.getAll();
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(1L, res.get(0).id());
        assertEquals(2L, res.get(1).id());
    }







}
