package ru.mahotin.service;

import ru.mahotin.web.dto.TaskDTO;

import java.util.List;


public interface TaskService {
    TaskDTO getById(Long id);
    List<TaskDTO> getAll();
    TaskDTO create(TaskDTO taskDTO);
    TaskDTO update(TaskDTO taskDTO, Long taskId);
    void delete(Long id);
}
