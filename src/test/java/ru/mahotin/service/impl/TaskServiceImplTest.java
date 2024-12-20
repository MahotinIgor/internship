package ru.mahotin.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mahotin.entity.Status;
import ru.mahotin.entity.Task;
import ru.mahotin.repository.TaskRepository;
import ru.mahotin.web.dto.TaskGetDTO;
import ru.mahotin.web.mapper.impl.TaskMapperImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapperImpl taskMapper;

    @BeforeEach()
    void setUp() {

        Task task = new Task();
        task.setId(2L);
        task.setTitle("mock status");
        task.setDescription("mock description");
        task.setStatus(Status.IN_PROGRESS);
        task.setUserId(1L);

        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(taskMapper.dtoFromEntity(task)).thenReturn(new TaskGetDTO(2L,
                "mock status",
                "mock description",
                "in_progress",
                1L));
    }

    @Test
    @DisplayName("Поиск Task по taskId")
    void getById() {

        TaskServiceImpl taskService = new TaskServiceImpl(taskRepository, taskMapper);
        TaskGetDTO res = taskService.getById(2L);
        assertNotNull(res);
    }

    @Test
    void getAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void updateStatusTask() {
    }
}