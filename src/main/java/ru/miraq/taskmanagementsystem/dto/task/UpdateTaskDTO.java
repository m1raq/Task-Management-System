package ru.miraq.taskmanagementsystem.dto.task;

import lombok.Data;
import ru.miraq.taskmanagementsystem.entity.task.TaskPriority;

@Data
public class UpdateTaskDTO {

    private String name;

    private String description;

    private TaskPriority priority;

    private String executorEmail;

}
