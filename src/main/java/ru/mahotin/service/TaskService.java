package ru.mahotin.service;

import ru.mahotin.entity.Task;

import java.util.List;


public interface TaskService {
    Task getById(Long id);
    List<Task> getAll();
    Task create(Task task);
    Task update(Task task, Long taskId);
    void delete(Long id);
}
