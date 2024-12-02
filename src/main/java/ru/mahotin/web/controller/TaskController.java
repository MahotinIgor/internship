package ru.mahotin.web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.mahotin.service.TaskService;
import ru.mahotin.web.dto.TaskDTO;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public TaskDTO getTaskById(
            @PathVariable("id") final Long id
    ) {
        return taskService.getById(id);
    }
    @GetMapping()
    public List<TaskDTO> getAllTask() {
        return taskService.getAll();
    }
    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO) {
        return taskService.create(taskDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteTaskById(
            @PathVariable("id") final Long id) {
        taskService.delete(id);
    }
    @PutMapping("/{id}")
    public TaskDTO updateTaskById(
            @PathVariable("id") final Long id,
            @RequestBody TaskDTO taskDTO
    ) {
        return taskService.update(taskDTO, id);

    }

}
