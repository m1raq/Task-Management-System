package ru.miraq.taskmanagementsystem.service;

import ru.miraq.taskmanagementsystem.dto.task.CreateTaskDTO;
import ru.miraq.taskmanagementsystem.dto.task.GetTaskDTO;
import ru.miraq.taskmanagementsystem.dto.task.UpdateTaskDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;


import java.util.List;


public interface TaskService {

    void createTask(CreateTaskDTO createTaskDTO, String email);

    void updateTask(UpdateTaskDTO updateTaskDTO, String ownerEmail);

    void setExecutor(String executorEmail, String taskName, String ownEmail);

    List<GetTaskDTO> getOwnTasks(String ownerEmail);

    List<GetTaskDTO> getSomeoneTasks(String ownerEmail);

    void deleteTask(String taskName, String ownerEmail);

    void updateTaskStatus(String taskName, TaskStatus taskStatus, String executorEmail);

    List<GetTaskDTO> getTasksByEmail(String email, String sortType);

    List<GetTaskDTO> getOwnTasksInProgress(String ownerEmail);

    TaskEntity getTaskByName(String taskName);

    boolean existByName(String taskName);


}
