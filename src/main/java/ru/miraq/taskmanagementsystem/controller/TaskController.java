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
import ru.miraq.taskmanagementsystem.dto.task.UpdateTaskDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;
import ru.miraq.taskmanagementsystem.service.TaskService;
import ru.miraq.taskmanagementsystem.service.TaskServiceImpl;


@Tag(name = "Задачи", description = "Контроллер для работы с задачами")
@RequestMapping("/api/task")
@RestController
public class TaskController {

    private final TaskService taskService;

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
        taskService.createTask(createTaskDTO, authentication.getName());
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message("Задача успешно создана")
                .build()
                , HttpStatus.CREATED);

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
        taskService.setExecutor(executorEmail, taskName, authentication.getName());
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message("Исполнитель задачи успешно назначен")
                .build(),HttpStatus.OK);

    }

    @Operation(
            summary = "Обновление задачи (Для заказчиков)",
            description = """
                   В теле запроса введите название задачи, которую необходимо отредактировать
                   и поля, которые необходимо изменить.
                   
                   Варианты поля status:\s
                   - WAITING
                   - IN_PROGRESS
                   - COMPLETED
                   
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
    public ResponseEntity<?> updateTask(@RequestBody UpdateTaskDTO updateTaskDTO
            , Authentication authentication){
        taskService.updateTask(updateTaskDTO, authentication.getName());
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message("Задача успешно обновлена")
                .build()
                , HttpStatus.OK);

    }

    @Operation(
            summary = "Получение собственных задач (Для заказчиков)"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/get-own-tasks")
    public ResponseEntity<?> getOwnTasks(Authentication authentication){
        return taskService.getOwnTasks(authentication.getName()).isEmpty()
                ? new ResponseEntity<>(ResponseMessageDTO.builder()
                .message("Ваш список задач пуст")
                .build()
                , HttpStatus.OK)
                : new ResponseEntity<>(taskService.getOwnTasks(authentication.getName()), HttpStatus.OK);

    }

    @Operation(
            summary = "Удаление собственных задач (Для заказчика)",
            description = "В параметр введите название задачи, которую необходимо удалить."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/delete-task")
    public ResponseEntity<ResponseMessageDTO> deleteTask(@RequestParam String taskName
            , Authentication authentication){
            taskService.deleteTask(taskName, authentication.getName());
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message("Задача успешно удалена")
                    .build()
                    ,HttpStatus.NO_CONTENT);

    }

    @Operation(
            summary = "Просмотр чужих задач (Для заказчиков)"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/get-someone-tasks")
    public ResponseEntity<?> getSomeoneTasks(Authentication authentication){
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
        taskService.updateTaskStatus(taskName, status, authentication.getName());
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message("Статус задачи успешно изменен")
                .build(),HttpStatus.OK);
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
        return new ResponseEntity<>(taskService.getTasksByEmail(email, sortType), HttpStatus.OK);
    }

    @Operation(summary = "Просмотр собственных задач, на которые данный пользователь назначен исполнителем "
            + "(Для исполнителей)")
    @PreAuthorize("hasAnyRole('EXECUTOR')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/get-own-tasks-in-progress")
    public ResponseEntity<?> getOwnTaskInProgress(Authentication authentication) {
        return new ResponseEntity<>(taskService.getOwnTasksInProgress(authentication.getName()), HttpStatus.OK);
    }



}
