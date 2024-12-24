//package ru.mahotin;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//
//import ru.mahotin.entity.Status;
//import ru.mahotin.entity.Task;
//import ru.mahotin.repository.TaskRepository;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//public class TaskControllerTest extends TestContainer {
//
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    TaskRepository taskRepository;


package ru.mahotin;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import ru.mahotin.entity.Status;
import ru.mahotin.entity.Task;
import ru.mahotin.repository.TaskRepository;

import java.util.Collections;
import java.util.Properties;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
    @AutoConfigureMockMvc
    @DirtiesContext
    @EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
    public class TaskControllerTest extends TestContainer {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        TaskRepository taskRepository;

        private static KafkaContainer kafkaContainer;
        private static KafkaProducer<String, String> producer;
        private static KafkaConsumer<String, String> consumer;

        @BeforeAll
        static void setUp() {

            kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));
            kafkaContainer.start();

            Properties producerProps = new Properties();
            producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
            producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producer = new KafkaProducer<>(producerProps);

            Properties consumerProps = new Properties();
            consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            consumer = new KafkaConsumer<>(consumerProps);
            consumer.subscribe(Collections.singletonList("test-topic"));

//        DataSource dataSource = new DriverManagerDataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
//        jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcTemplate.execute("CREATE TABLE test_table (id SERIAL PRIMARY KEY, value VARCHAR(255))");
        }

        @AfterAll
        static void tearDown() {
            producer.close();
            consumer.close();
            kafkaContainer.stop();
        }

        @Test
        @DisplayName("Создание Task")
        void createTask() throws Exception {
            String taskJson = "{\"title\":\"title\",\"description\":\"description\",\"status\":\"new\",\"userId\":1}";

            mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(taskJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("title"))
                    .andExpect(jsonPath("$.description").value("description"))
                    .andExpect(jsonPath("$.status").value("new"))
                    .andExpect(jsonPath("$.userId").value(1));
        }

        @Test
        @DisplayName("Обновление Task")
        void updateTask() throws Exception {
            Task task = new Task();
            task.setTitle("title");
            task.setDescription("description");
            task.setStatus(Status.IN_PROGRESS);
            task.setUserId(1L);
            taskRepository.save(task);

            String taskJson = "{\"title\":\"updated title\",\"description\":\"updated description\",\"status\":\"in_progress\",\"userId\":1}";

            mockMvc.perform(put("/api/tasks/" + task.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(taskJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("updated title"))
                    .andExpect(jsonPath("$.description").value("updated description"))
                    .andExpect(jsonPath("$.status").value("in_progress"))
                    .andExpect(jsonPath("$.userId").value(1));
        }

        @Test
        @DisplayName("Обновление статуса Task")
        void updateStatusTask() throws Exception {
            Task task = new Task();
            task.setTitle("title");
            task.setDescription("description");
            task.setStatus(Status.IN_PROGRESS);
            task.setUserId(1L);
            Task resTask = taskRepository.save(task);

            mockMvc.perform(put("/api/tasks" )
                            .param("id", resTask.getId().toString())
                            .param("status", "done"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("done"));
        }

        @Test
        @DisplayName("Удаление Task")
        void deleteTask() throws Exception {
            Task task = new Task();
            task.setTitle("title");
            task.setDescription("description");
            task.setStatus(Status.IN_PROGRESS);
            task.setUserId(1L);
            taskRepository.save(task);

            mockMvc.perform(delete("/api/tasks/" + task.getId()))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/tasks/" + task.getId()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Поиск всех Task")
        void getAllTasks() throws Exception {
            Task task1 = new Task();
            task1.setTitle("title1");
            task1.setDescription("description1");
            task1.setStatus(Status.IN_PROGRESS);
            task1.setUserId(1L);
            taskRepository.save(task1);

            Task task2 = new Task();
            task2.setTitle("title2");
            task2.setDescription("description2");
            task2.setStatus(Status.NEW);
            task2.setUserId(2L);
            taskRepository.save(task2);

            mockMvc.perform(get("/api/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].title").value("title1"))
                    .andExpect(jsonPath("$[1].title").value("title2"));
        }

    }

