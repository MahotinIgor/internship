package ru.mahotin.service.impl;

import org.springframework.stereotype.Service;
import ru.mahotin.aspect.LogCreateEntity;
import ru.mahotin.aspect.LogDeleteTask;
import ru.mahotin.aspect.LogErrorGetTaskById;
import ru.mahotin.entity.Status;
import ru.mahotin.repository.TaskRepository;
import ru.mahotin.entity.Task;
import ru.mahotin.exception.TaskNotFoundException;
import ru.mahotin.service.TaskService;
import ru.mahotin.web.dto.TaskGetDTO;
import ru.mahotin.web.dto.TaskUpdateDTO;
import ru.mahotin.web.mapper.impl.TaskMapperImpl;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapperImpl taskMapper;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            TaskMapperImpl taskMapper
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    @LogErrorGetTaskById
    public TaskGetDTO getById(
            final Long id
    ) {
         Task task = taskRepository
                .findById(id)
                .orElseThrow(
                        () -> new TaskNotFoundException("User not found")
                );
         return taskMapper.dtoFromEntity(task);
    }

    @Override
    public List<TaskGetDTO> getAll() {
        return taskRepository
                .findAll()
                    .stream()
                    .map(taskMapper::dtoFromEntity)
                    .toList();
    }

    @Override
    @LogCreateEntity
    public TaskGetDTO create(
            final TaskUpdateDTO taskUpdateDTO
    ) {
        Task task = taskMapper.entityFromDto(taskUpdateDTO);
         return taskMapper
                 .dtoFromEntity(
                         taskRepository.save(task)
                 );
    }

    @Override
    @LogErrorGetTaskById
    public TaskGetDTO update(
            final TaskUpdateDTO taskUpdateDTO,
            final Long taskId
    ) {

        Task taskInDB = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new TaskNotFoundException("User not found"));
        taskInDB.setTitle(taskUpdateDTO.title());
        taskInDB.setDescription(taskUpdateDTO.description());
        taskInDB.setUserId(taskUpdateDTO.userId());
        return taskMapper
                .dtoFromEntity(
                        taskRepository.save(taskInDB)
                );
    }

    @Override
    @LogDeleteTask
    public void delete(final Long id) {
        taskRepository
                .findById(id)
                .orElseThrow(
                        () -> new TaskNotFoundException("User not found"));
        taskRepository.deleteById(id);
    }

    @Override
    public TaskGetDTO updateStatusTask(Long taskId, String status) {
        Task taskInDB = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new TaskNotFoundException("User not found"));
        taskInDB.setStatus(Status.fromText(status));
        return taskMapper
                .dtoFromEntity(
                        taskRepository.save(taskInDB)
                );
    }
}
