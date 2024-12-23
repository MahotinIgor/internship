package ru.mahotin.web.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mahotin.entity.Status;
import ru.mahotin.entity.Task;
import ru.mahotin.web.dto.TaskGetDTO;
import ru.mahotin.web.dto.TaskUpdateDTO;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperImplTest {

    private TaskMapperImpl taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapperImpl();
    }

    @Test
    void entityFromDto() {
        TaskUpdateDTO dto = new TaskUpdateDTO("title",
                "description",
                "new",
                1L);
        Task task = taskMapper.entityFromDto(dto);

        assertNotNull(task);
        assertEquals(dto.title(), task.getTitle());
        assertEquals(dto.description(), task.getDescription());
        assertEquals(Status.fromText(dto.status()), task.getStatus());
        assertEquals(dto.userId(), task.getUserId());
    }

    @Test
    void dtoFromEntity() {
        Task task = new Task(1L,
                "title",
                "description",
                Status.fromText("new"),
                1L);
        TaskGetDTO dto = taskMapper.dtoFromEntity(task);

        assertNotNull(dto);
        assertEquals(task.getId(), dto.id());
        assertEquals(task.getTitle(), dto.title());
        assertEquals(task.getDescription(), dto.description());
        assertEquals(task.getStatus().getTitle(), dto.status());
        assertEquals(task.getUserId(), dto.userId());
    }
}