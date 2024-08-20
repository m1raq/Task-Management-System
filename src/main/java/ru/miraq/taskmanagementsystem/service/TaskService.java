package ru.miraq.taskmanagementsystem.service;

import ru.miraq.taskmanagementsystem.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    void createTask(TaskDTO taskDTO, String email);

    void updateTask(TaskDTO taskDTO);

    void deleteTaskById(Long id);

    void setExecutor(String executorEmail, Long taskId);

    TaskDTO getTaskById(Long id);

    List<TaskDTO> getAllTasks();
}
