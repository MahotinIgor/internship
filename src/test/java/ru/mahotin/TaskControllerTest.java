//package ru.mahotin;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//
//import ru.mahotin.entity.Status;
//import ru.mahotin.entity.Task;
//import ru.mahotin.repository.TaskRepository;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//public class TaskControllerTest extends TestContainer {
//
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    TaskRepository taskRepository;
//
//
//    package ru.mahotin;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.mahotin.entity.Status;
//import ru.mahotin.entity.Task;
//import ru.mahotin.repository.TaskRepository;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//    @AutoConfigureMockMvc
//    @DirtiesContext
//    public class TaskControllerTest extends TestContainer {
//
//        @Autowired
//        MockMvc mockMvc;
//
//        @Autowired
//        TaskRepository taskRepository;
//
//        @Test
//        @DisplayName("Создание Task")
//        void createTask() throws Exception {
//            String taskJson = "{\"title\":\"title\",\"description\":\"description\",\"status\":\"in_progress\",\"userId\":1}";
//
//            mockMvc.perform(post("/tasks")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(taskJson))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.title").value("title"))
//                    .andExpect(jsonPath("$.description").value("description"))
//                    .andExpect(jsonPath("$.status").value("in_progress"))
//                    .andExpect(jsonPath("$.userId").value(1));
//        }
//
//        @Test
//        @DisplayName("Обновление Task")
//        void updateTask() throws Exception {
//            Task task = new Task();
//            task.setTitle("title");
//            task.setDescription("description");
//            task.setStatus(Status.IN_PROGRESS);
//            task.setUserId(1L);
//            taskRepository.save(task);
//
//            String taskJson = "{\"title\":\"updated title\",\"description\":\"updated description\",\"status\":\"in_progress\",\"userId\":1}";
//
//            mockMvc.perform(put("/tasks/" + task.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(taskJson))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.title").value("updated title"))
//                    .andExpect(jsonPath("$.description").value("updated description"))
//                    .andExpect(jsonPath("$.status").value("in_progress"))
//                    .andExpect(jsonPath("$.userId").value(1));
//        }
//
//        @Test
//        @DisplayName("Обновление статуса Task")
//        void updateStatusTask() throws Exception {
//            Task task = new Task();
//            task.setTitle("title");
//            task.setDescription("description");
//            task.setStatus(Status.IN_PROGRESS);
//            task.setUserId(1L);
//            taskRepository.save(task);
//
//            mockMvc.perform(patch("/tasks/" + task.getId() + "/status")
//                            .param("status", "completed"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("completed"));
//        }
//
//        @Test
//        @DisplayName("Удаление Task")
//        void deleteTask() throws Exception {
//            Task task = new Task();
//            task.setTitle("title");
//            task.setDescription("description");
//            task.setStatus(Status.IN_PROGRESS);
//            task.setUserId(1L);
//            taskRepository.save(task);
//
//            mockMvc.perform(delete("/tasks/" + task.getId()))
//                    .andExpect(status().isOk());
//
//            mockMvc.perform(get("/tasks/" + task.getId()))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("Поиск всех Task")
//        void getAllTasks() throws Exception {
//            Task task1 = new Task();
//            task1.setTitle("title1");
//            task1.setDescription("description1");
//            task1.setStatus(Status.IN_PROGRESS);
//            task1.setUserId(1L);
//            taskRepository.save(task1);
//
//            Task task2 = new Task();
//            task2.setTitle("title2");
//            task2.setDescription("description2");
//            task2.setStatus(Status.NEW);
//            task2.setUserId(2L);
//            taskRepository.save(task2);
//
//            mockMvc.perform(get("/tasks"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.length()").value(2))
//                    .andExpect(jsonPath("$[0].title").value("title1"))
//                    .andExpect(jsonPath("$[1].title").value("title2"));
//        }
//    }
//
//
//
//}
