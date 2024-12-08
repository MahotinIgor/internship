package ru.mahotin.web.mapper.impl;

import org.springframework.stereotype.Service;
import ru.mahotin.aspect.LogTaskMapper;
import ru.mahotin.entity.Task;
import ru.mahotin.web.dto.TaskGetDTO;
import ru.mahotin.web.dto.TaskUpdateDTO;
import ru.mahotin.web.mapper.TaskMapper;
@Service
public class TaskMapperImpl implements TaskMapper<Task,TaskUpdateDTO, TaskGetDTO> {

    public Task entityFromDto(TaskUpdateDTO dto) {
        return new Task(
                dto.title(),
                dto.description(),
                dto.userId()
        );
    }
    @Override
    @LogTaskMapper
    public TaskGetDTO dtoFromEntity(Task entity) {
        return new TaskGetDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getUserId()
        );
    }
}
