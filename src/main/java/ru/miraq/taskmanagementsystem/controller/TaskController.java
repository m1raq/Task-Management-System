package ru.miraq.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.miraq.taskmanagementsystem.dto.ResponseMessageDTO;
import ru.miraq.taskmanagementsystem.dto.task.CreateTaskDTO;
import ru.miraq.taskmanagementsystem.dto.task.GetTaskDTO;
import ru.miraq.taskmanagementsystem.dto.task.UpdateTaskDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;
import ru.miraq.taskmanagementsystem.exception.CreateTaskException;
import ru.miraq.taskmanagementsystem.exception.InputParamException;
import ru.miraq.taskmanagementsystem.exception.TaskNotFoundException;
import ru.miraq.taskmanagementsystem.service.TaskServiceImpl;

import java.util.List;

@Tag(name = "Задачи", description = "Контроллер для работы с задачами")
@RequestMapping("/api/task")
@RestController
public class TaskController {

    private final TaskServiceImpl taskService;

    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }


    @Operation(
            summary = "Создание задачи (Для заказчиков)",
            description = """
            При создании задачи изначально status равен WAITING.
          
            Варианты поля priority:\s
             - LOW
             - MEDIUM
             - HIGH""")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestBody CreateTaskDTO createTaskDTO
            , Authentication authentication){
        try {
            taskService.createTask(createTaskDTO, authentication.getName());
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message("Задача успешно создана")
                    .build()
                    , HttpStatus.CREATED);
        } catch (CreateTaskException e) {
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Назначение исполнителя задачи (Для заказчиков)",
            description = "При назначении значение поля задачи status меняется на IN_PROGRESS "
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/set-executor")
    public ResponseEntity<?> setExecutorTask(@RequestParam String executorEmail, @RequestParam String taskName
            , Authentication authentication){
        try {
            taskService.setExecutor(executorEmail, taskName, authentication.getName());
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message("Исполнитель задачи успешно назначен")
                    .build(),HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message("Задача не найдена"), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Обновление задачи (Для заказчиков)",
            description = """
                   В теле запроса введите id задачи, которую необходимо отредактировать
                   и поля, которые необходимо изменить.
                   
                   Варианты поля priority:\s
                   - LOW
                   - MEDIUM
                   - HIGH
                   
                   Варианты поля executorEmail:
                   - 1 (Задание выполнено. Исполнитель удален. Статус будет изменен на COMPLETED)
                   - 0 (Задание не выполнено. Исполнитель удален. Статус будет изменен на WAITING)
                   - Email исполнителя
                   """
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/update-task")
    public ResponseEntity<HttpStatus> updateTask(@RequestBody UpdateTaskDTO updateTaskDTO
            , Authentication authentication){
        taskService.updateTask(updateTaskDTO, authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Получение собственных задач (Для заказчиков)"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/get-own-tasks")
    public ResponseEntity<List<GetTaskDTO>> getOwnTasks(Authentication authentication){
        return new ResponseEntity<>(taskService.getOwnTasks(authentication.getName()), HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление собственных задач (Для заказчика)",
            description = "В параметр введите название задачи, которую необходимо удалить."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/delete-task")
    public ResponseEntity<HttpStatus> deleteTask(@RequestParam String taskName, Authentication authentication){
        taskService.deleteTask(taskName, authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Просмотр чужих задач (Для заказчиков)"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/get-someone-tasks")
    public ResponseEntity<List<GetTaskDTO>> getSomeoneTasks(Authentication authentication){
        return new ResponseEntity<>(taskService.getSomeoneTasks(authentication.getName()), HttpStatus.OK);
    }

    @Operation(
            summary = "Обновление статуса задачи (Для исполнителей)",
            description = """
                   Обновление статуса задачи.
                   
                   Варианты поля status:\s
                   - WAITING
                   - COMPLETED
                   - IN_PROGRESS
                   """
    )
    @PreAuthorize("hasRole('EXECUTOR')")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/update-task-status")
    public ResponseEntity<?> updateTaskStatus(String taskName, TaskStatus status, Authentication authentication) {
        try {
            taskService.updateTaskStatus(taskName, status, authentication.getName());
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message("Статус задачи успешно изменен")
                    .build(),HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message("Задача не найдена")
                    .build(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Просмотр задач заказчика/исполнителя",
            description = """
                   В поле email введите email пользователя, задачи которого необходимо получить
                   
                   Варианты поля sortType:\s
                   - 1 (Возвращает задачи созданные данным пользователем)
                   - 2 (Возвращает задачи, на которые данный пользователь назначен исполнителем)
                 
                   """
    )
    @PreAuthorize("hasAnyRole('EXECUTOR', 'CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/get-tasks-by-email")
    public ResponseEntity<?> getTasksByEmail(@RequestParam String email, @RequestParam String sortType){
        try {
            return new ResponseEntity<>(taskService.getTasksByEmail(email, sortType), HttpStatus.OK);
        } catch (InputParamException e) {
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Просмотр собственных задач, на которые данный пользователь назначен исполнителем (Для исполнителей)")
    @PreAuthorize("hasAnyRole('EXECUTOR')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/get-own-tasks-in-progress")
    public ResponseEntity<List<GetTaskDTO>> getOwnTaskInProgress(Authentication authentication) {
        return new ResponseEntity<>(taskService.getOwnTasksInProgress(authentication.getName()), HttpStatus.OK);
    }



}
