package ru.miraq.taskmanagementsystem.dto.task;

import lombok.Data;
import ru.miraq.taskmanagementsystem.dto.comment.CommentDTO;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskPriority;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;

import java.util.List;

@Data
public class TaskDTO {

    private Long id;

    private String name;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private UserDTO author;

    private UserDTO executor;

    private List<CommentDTO> comments;
}
