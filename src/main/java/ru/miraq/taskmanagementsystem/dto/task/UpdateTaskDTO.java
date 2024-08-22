package ru.miraq.taskmanagementsystem.dto.task;

import lombok.Data;
import ru.miraq.taskmanagementsystem.entity.task.TaskPriority;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;

@Data
public class UpdateTaskDTO {

    private String name;

    private String description;

    private TaskStatus taskStatus;

    private TaskPriority priority;

    private String executorEmail;

}
