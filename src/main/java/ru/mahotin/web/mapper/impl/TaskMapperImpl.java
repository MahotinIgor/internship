package ru.mahotin.web.mapper.impl;

import org.springframework.stereotype.Service;
import ru.mahotin.entity.Task;
import ru.mahotin.web.dto.TaskDTO;
import ru.mahotin.web.mapper.TaskMapper;
@Service
public class TaskMapperImpl implements TaskMapper<Task, TaskDTO> {

    public Task entityFromDto(TaskDTO dto) {
        return new Task(
                dto.title(),
                dto.description(),
                dto.userId()
        );
    }
    @Override
    public TaskDTO dtoFromEntity(Task entity) {
        return new TaskDTO(
                entity.getTitle(),
                entity.getDescription(),
                entity.getUserId()
        );
    }
}
