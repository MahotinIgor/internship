package ru.mahotin.service.impl;

import org.springframework.stereotype.Service;
import ru.mahotin.repository.TaskRepository;
import ru.mahotin.entity.Task;
import ru.mahotin.exception.ResourceNotFoundException;
import ru.mahotin.service.TaskService;
import ru.mahotin.web.dto.TaskDTO;
import ru.mahotin.web.mapper.impl.TaskMapperImpl;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapperImpl taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapperImpl taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO getById(
            final Long id
    ) {
         Task task = taskRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
         return taskMapper.dtoFromEntity(task);
    }

    @Override
    public List<TaskDTO> getAll() {
        return taskRepository
                .findAll()
                    .stream()
                    .map(taskMapper::dtoFromEntity)
                    .toList();
    }

    @Override
    public TaskDTO create(
            final TaskDTO taskDTO
    ) {
        Task task = taskMapper.entityFromDto(taskDTO);
         return taskMapper
                 .dtoFromEntity(
                         taskRepository.save(task)
                 );
    }

    @Override
    public TaskDTO update(final TaskDTO taskDTO, final Long taskId) {

        Task taskInDB = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found"));
        taskInDB.setTitle(taskDTO.title());
        taskInDB.setDescription(taskDTO.description());
        taskInDB.setUserId(taskDTO.userId());
        return taskMapper
                .dtoFromEntity(
                        taskRepository.save(taskInDB)
                );
    }

    @Override
    public void delete(
            final Long id
    ) {
        taskRepository.deleteById(id);
    }
}
