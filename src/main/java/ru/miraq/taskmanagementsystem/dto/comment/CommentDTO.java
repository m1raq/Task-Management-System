package ru.miraq.taskmanagementsystem.dto.comment;

import lombok.Data;
import ru.miraq.taskmanagementsystem.dto.task.TaskDTO;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;


@Data
public class CommentDTO {

    private Long id;

    private UserDTO author;

    private String text;

    private TaskDTO task;
}
