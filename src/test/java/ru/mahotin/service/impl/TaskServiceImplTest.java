package ru.mahotin.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mahotin.entity.Status;
import ru.mahotin.entity.Task;
import ru.mahotin.exception.TaskNotFoundException;
import ru.mahotin.repository.TaskRepository;
import ru.mahotin.web.dto.TaskGetDTO;
import ru.mahotin.web.dto.TaskUpdateDTO;
import ru.mahotin.web.mapper.impl.TaskMapperImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapperImpl taskMapper;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("Поиск Task по taskId")
    void getById() {
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

        TaskGetDTO res = taskService.getById(2L);
        assertNotNull(res);
        assertEquals(2L, res.id());
        assertEquals("mock status", res.title());
        assertEquals("mock description", res.description());
        assertEquals("in_progress", res.status());
        assertEquals(1L, res.userId());
    }
    @Test
    @DisplayName("Поиск Task по taskId: TaskNotFoundException")
    void getByIdTaskNotFound() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getById(2L));
    }

    @Test
    @DisplayName("Поиск всех Task")
    void getAll() {
        Task task1 = new Task();
        task1.setId(2L);
        task1.setTitle("mock status 1");
        task1.setDescription("mock description 1");
        task1.setStatus(Status.IN_PROGRESS);
        task1.setUserId(1L);

        Task task2 = new Task();
        task2.setId(3L);
        task2.setTitle("mock status 2");
        task2.setDescription("mock description 2");
        task2.setStatus(Status.NEW);
        task2.setUserId(2L);

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));
        when(taskMapper.dtoFromEntity(task1)).thenReturn(new TaskGetDTO(2L,
                "mock status 1",
                "mock description 1",
                "in_progress",
                1L));
        when(taskMapper.dtoFromEntity(task2)).thenReturn(new TaskGetDTO(3L,
                "mock status 2",
                "mock description 2",
                "new",
                2L));

        List<TaskGetDTO> res = taskService.getAll();
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(2L, res.get(0).id());
        assertEquals(3L, res.get(1).id());
    }

    @Test
    @DisplayName("Создание Task")
    void create() {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO(
                "title",
                "description",
                "status",
                1L);
        Task task = new Task();
        task.setId(2L);
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(Status.IN_PROGRESS);
        task.setUserId(1L);

        when(taskMapper.entityFromDto(taskUpdateDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.dtoFromEntity(task)).thenReturn(
                new TaskGetDTO(2L,
                        "title",
                        "description",
                        "in_progress",
                        1L));

        TaskGetDTO res = taskService.create(taskUpdateDTO);
        assertNotNull(res);
        assertEquals(2L, res.id());
        assertEquals("title", res.title());
        assertEquals("description", res.description());
        assertEquals("in_progress", res.status());
        assertEquals(1L, res.userId());
    }

    @Test
    @DisplayName("Обновление Task")
    void update() {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO(
                "updated title",
                "updated description",
                "status",
                1L);
        Task task = new Task();
        task.setId(2L);
        task.setTitle("mock status");
        task.setDescription("mock description");
        task.setStatus(Status.IN_PROGRESS);
        task.setUserId(1L);

        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.dtoFromEntity(task)).thenReturn(new TaskGetDTO(2L,
                "updated title",
                "updated description",
                "in_progress",
                1L));

        TaskGetDTO res = taskService.update(taskUpdateDTO, 2L);
        assertNotNull(res);
        assertEquals(2L, res.id());
        assertEquals("updated title", res.title());
        assertEquals("updated description", res.description());
        assertEquals("in_progress", res.status());
        assertEquals(1L, res.userId());
    }

    @Test
    @DisplayName("Обновление Task: TaskNotFoundException")
    void updateTaskNotFound() {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO(
                "updated title",
                "updated description",
                "status",
                1L);

        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.update(taskUpdateDTO, 2L));
    }

    @Test
    @DisplayName("Удаление Task")
    void delete() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("mock title");
        task.setDescription("mock description");
        task.setStatus(Status.IN_PROGRESS);
        task.setUserId(1L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        taskService.delete(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    @DisplayName("Удаление Task: TaskNotFoundException")
    void deleteTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.delete(1L));
        verify(taskRepository, never()).deleteById(1L);
    }

    @Test
    @DisplayName("Обновление статуса Task")
    void updateStatusTask() {
        Long taskId = 2L;
        String newStatus = "done";
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("mock status");
        task.setDescription("mock description");
        task.setStatus(Status.IN_PROGRESS);
        task.setUserId(1L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.dtoFromEntity(task)).thenReturn(new TaskGetDTO(taskId,
                "mock status",
                "mock description",
                newStatus,
                1L));

        TaskGetDTO res = taskService.updateStatusTask(taskId, newStatus);
        assertNotNull(res);
        assertEquals(taskId, res.id());
        assertEquals("mock status", res.title());
        assertEquals("mock description", res.description());
        assertEquals(newStatus, res.status());
        assertEquals(1L, res.userId());
    }
}