package ru.mahotin.web.controller;



import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mahotin.service.TaskService;
import ru.mahotin.web.dto.TaskGetDTO;
import ru.mahotin.web.dto.TaskUpdateDTO;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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

}
