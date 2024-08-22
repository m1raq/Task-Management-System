package ru.miraq.taskmanagementsystem.dto.task;

import lombok.Data;
import ru.miraq.taskmanagementsystem.entity.task.TaskPriority;

@Data
public class CreateTaskDTO {

    private String name;

    private String description;

    private TaskPriority priority;

}
