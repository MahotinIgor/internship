package ru.mahotin;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.mahotin.entity.Status;
import ru.mahotin.entity.Task;
import ru.mahotin.repository.TaskRepository;
import ru.mahotin.service.impl.TaskServiceImpl;
import ru.mahotin.web.dto.TaskGetDTO;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = Main.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class DemoApplicationTests extends TestContainer
{

    @Autowired
    TaskServiceImpl taskService;

    @Autowired
    TaskRepository taskRepository;

    private static KafkaContainer kafkaContainer;
    private static KafkaProducer<String, String> producer;
    private static KafkaConsumer<String, String> consumer;
    private static JdbcTemplate jdbcTemplate;

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

        DataSource dataSource = new DriverManagerDataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("CREATE TABLE test_table (id SERIAL PRIMARY KEY, value VARCHAR(255))");
    }

    @AfterAll
    static void tearDown() {
        producer.close();
        consumer.close();
        kafkaContainer.stop();
    }

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

        List<TaskGetDTO> res = taskService.getAll();
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(1L, res.get(0).id());
        assertEquals(2L, res.get(1).id());
    }

    @Test
    @DisplayName("Kafka and PostgreSQL Integration Test")
    void testKafkaAndPostgresIntegration() {
        String topic = "test-topic";
        String key = "key";
        String value = "value";

        producer.send(new ProducerRecord<>(topic, key, value));
        producer.flush();

        consumer.poll(Duration.ofSeconds(10)).forEach(record -> {
            assertEquals(key, record.key());
            assertEquals(value, record.value());
        });

        jdbcTemplate.update("INSERT INTO test_table (value) VALUES (?)", value);
        String dbValue = jdbcTemplate.queryForObject("SELECT value FROM test_table WHERE value = ?", new Object[]{value}, String.class);
        assertEquals(value, dbValue);
    }
}