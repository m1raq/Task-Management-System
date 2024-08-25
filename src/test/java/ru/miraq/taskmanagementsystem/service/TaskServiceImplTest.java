package ru.miraq.taskmanagementsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.miraq.taskmanagementsystem.dto.task.CreateTaskDTO;
import ru.miraq.taskmanagementsystem.dto.task.TaskDTO;
import ru.miraq.taskmanagementsystem.dto.task.UpdateTaskDTO;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;
import ru.miraq.taskmanagementsystem.entity.task.TaskPriority;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;
import ru.miraq.taskmanagementsystem.entity.user.RoleType;
import ru.miraq.taskmanagementsystem.exception.CreateTaskException;
import ru.miraq.taskmanagementsystem.mapper.TaskMapper;
import ru.miraq.taskmanagementsystem.repository.TaskRepository;
import ru.miraq.taskmanagementsystem.repository.UserRepository;
import ru.miraq.taskmanagementsystem.security.SecurityService;
import ru.miraq.taskmanagementsystem.security.dto.AuthRequestDTO;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private TaskMapper taskMapper;

    @BeforeEach
    public void registerUser() {
        AuthRequestDTO customer = new AuthRequestDTO();
        customer.setEmail("test@mail.ru");
        customer.setPassword("password");

        AuthRequestDTO executor = new AuthRequestDTO();
        executor.setEmail("test2@mail.ru");
        executor.setPassword("password");

        securityService.register(customer, RoleType.ROLE_CUSTOMER);
        securityService.register(executor, RoleType.ROLE_EXECUTOR);
    }

    @AfterEach
    public void cleanUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Тест создания задачи")
    @Test
    public void createTaskTest() {
        CreateTaskDTO task = new CreateTaskDTO();
        task.setName("test");
        task.setDescription("test");
        task.setPriority(TaskPriority.LOW);

        when(taskRepository.existsByName("test")).thenReturn(true);
        doReturn(new TaskEntity()).when(taskRepository).save(isNull());

        taskService.createTask(task, "test@mail.ru");

        assertTrue(taskRepository.existsByName("test"));
        verify(taskRepository, times(1)).save(isNull());
    }


    @DisplayName("Тест неверный формат при создании задачи")
    @Test
    public void createTaskWrongFormatTest() {
        CreateTaskDTO task = new CreateTaskDTO();
        task.setName("");
        task.setDescription("test");
        task.setPriority(TaskPriority.LOW);

        assertThrows(CreateTaskException.class, () -> taskService.createTask(task, "test@mail.ru"));
        assertFalse(taskRepository.existsByName(task.getName()));
    }

    @DisplayName("Тест на обновление задачи")
    @Test
    public void updateTaskTest() {
        CreateTaskDTO task = new CreateTaskDTO();
        task.setName("test");
        task.setDescription("test");
        task.setPriority(TaskPriority.LOW);

        UserDTO userDTO = new UserDTO();
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test");
        taskDTO.setStatus(TaskStatus.WAITING);
        taskDTO.setPriority(TaskPriority.LOW);
        taskDTO.setDescription("test");
        userDTO.setTask(Collections.singletonList(taskDTO));

        when(userService.getUserByEmail("test@mail.ru")).thenReturn(userDTO);

        taskService.createTask(task, "test@mail.ru");

        UpdateTaskDTO updateTask = new UpdateTaskDTO();
        updateTask.setName("test");
        updateTask.setDescription("test2");
        updateTask.setTaskStatus(TaskStatus.WAITING);
        updateTask.setPriority(TaskPriority.HIGH);
        updateTask.setExecutorEmail("1");

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setDescription("test2");

        doReturn(taskEntity).when(taskRepository).save(any());
        when(taskRepository.findByName(updateTask.getName())).thenReturn(Optional.of(taskEntity));

        taskService.updateTask(updateTask, "test@mail.ru");

        assertEquals("test2", taskRepository.findByName(updateTask.getName())
                .orElse(new TaskEntity())
                .getDescription());
    }

}


