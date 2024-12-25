package ru.mahotin.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mahotin.exception.TaskNotFoundException;
import ru.mahotin.kafka.KafkaServiceProducer;
import ru.mahotin.service.TaskService;
import ru.mahotin.web.dto.StatusTaskDTO;
import ru.mahotin.web.dto.TaskGetDTO;
import ru.mahotin.web.dto.TaskUpdateDTO;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Value("${spring.kafka.statusTopic}")
    private String kafkaTopic;
    private final TaskService taskService;
    private final KafkaServiceProducer kafkaServiceProducer;

    public TaskController(TaskService taskService, KafkaServiceProducer kafkaServiceProducer) {
        this.taskService = taskService;
        this.kafkaServiceProducer = kafkaServiceProducer;
    }

     @GetMapping("/{id}")
    public TaskGetDTO getTaskById(
            @PathVariable("id") final Long id
    ) {
        return taskService.getById(id);
    }
    @GetMapping()
    public List<TaskGetDTO> getAllTask() {
        return taskService.getAll();
    }
    @PostMapping
    public TaskGetDTO createTask(@RequestBody final TaskUpdateDTO taskUpdateDTO) {
        return taskService.create(taskUpdateDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteTaskById(
            @PathVariable("id") final Long id) {
        taskService.delete(id);
    }
    @PutMapping("/{id}")
    public TaskGetDTO updateTaskById(
            @PathVariable("id") final Long id,
            @RequestBody TaskUpdateDTO taskUpdateDTO
    ) {
        return taskService.update(taskUpdateDTO, id);

    }

    @PutMapping()
    public TaskGetDTO updateStatusTaskById(
            @RequestParam("id") final Long id,
            @RequestParam("status") final String status
    ) {
        TaskGetDTO updatedTask =  taskService.updateStatusTask(id, status);
        StatusTaskDTO newTaskStatus = new StatusTaskDTO(
                updatedTask.id(),
                updatedTask.status()
        );

        kafkaServiceProducer.sendMessageWithCallBack(
                kafkaTopic,
                newTaskStatus
        );
        return updatedTask;
    }
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
