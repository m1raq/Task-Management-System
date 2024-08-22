package ru.miraq.taskmanagementsystem.service;

import ru.miraq.taskmanagementsystem.dto.task.CreateTaskDTO;
import ru.miraq.taskmanagementsystem.dto.task.GetTaskDTO;
import ru.miraq.taskmanagementsystem.dto.task.UpdateTaskDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;
import ru.miraq.taskmanagementsystem.exception.CreateTaskException;
import ru.miraq.taskmanagementsystem.exception.InputParamException;
import ru.miraq.taskmanagementsystem.exception.TaskNotFoundException;

import java.util.List;


public interface TaskService {

    void createTask(CreateTaskDTO createTaskDTO, String email) throws CreateTaskException;

    void updateTask(UpdateTaskDTO updateTaskDTO, String ownerEmail);

    void setExecutor(String executorEmail, String taskName, String ownEmail) throws TaskNotFoundException;

    List<GetTaskDTO> getOwnTasks(String ownerEmail);

    List<GetTaskDTO> getSomeoneTasks(String ownerEmail);

    void deleteTask(String taskName, String ownerEmail);

    void updateTaskStatus(String taskName, TaskStatus taskStatus, String executorEmail) throws TaskNotFoundException;

    List<GetTaskDTO> getTasksByEmail(String email, String sortType) throws InputParamException;

    List<GetTaskDTO> getOwnTasksInProgress(String ownerEmail);

    TaskEntity getTaskByName(String taskName);


}
