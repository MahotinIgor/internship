package ru.mahotin.service.impl;

import org.springframework.stereotype.Service;
import ru.mahotin.TaskRepository;
import ru.mahotin.entity.Task;
import ru.mahotin.exception.ResourceNotFoundException;
import ru.mahotin.service.TaskService;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task getById(
            final Long id
    ) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task create(
            final Task task
    ) {
        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task, Long taskId) {

        Task taskInDB = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found"));
        taskInDB.setTitle(task.getTitle());
        taskInDB.setDescription(taskInDB.getDescription());
        taskInDB.setUserId(task.getUserId());
        return taskRepository.save(taskInDB);
    }

    @Override
    public void delete(
            final Long id
    ) {
        taskRepository.deleteById(id);
    }
}
