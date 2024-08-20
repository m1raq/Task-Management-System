package ru.miraq.taskmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.miraq.taskmanagementsystem.dto.TaskDTO;
import ru.miraq.taskmanagementsystem.service.TaskServiceImpl;

@RequestMapping("/api/task")
@RestController
public class TaskController {

    private final TaskServiceImpl taskService;

    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/add")
    public void addTask(@RequestBody TaskDTO taskDTO, Authentication authentication){
        taskService.createTask(taskDTO, authentication.getName());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/set-executor/{}")
    public void setExecutorTask(@PathVariable String email, @PathVariable Long taskId){
        taskService.setExecutor(email, taskId);
    }

}
