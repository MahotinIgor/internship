package ru.mahotin.service;

import ru.mahotin.web.dto.TaskGetDTO;
import ru.mahotin.web.dto.TaskUpdateDTO;

import java.util.List;


public interface TaskService {
    TaskGetDTO getById(Long id);
    List<TaskGetDTO> getAll();
    TaskGetDTO create(TaskUpdateDTO taskUpdateDTO);
    TaskGetDTO update(TaskUpdateDTO taskUpdateDTO, Long taskId);
    void delete(Long id);
}
