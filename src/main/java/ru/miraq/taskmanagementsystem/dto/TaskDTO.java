package ru.miraq.taskmanagementsystem.dto;

import lombok.Data;
import ru.miraq.taskmanagementsystem.entity.task.TaskPriority;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;

@Data
public class TaskDTO {

    private Long id;

    private String name;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private UserDTO author;

    private UserDTO executor;
}
