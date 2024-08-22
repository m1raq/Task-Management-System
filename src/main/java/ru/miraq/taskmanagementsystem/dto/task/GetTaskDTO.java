package ru.miraq.taskmanagementsystem.dto.task;

import lombok.Builder;
import lombok.Data;
import ru.miraq.taskmanagementsystem.dto.comment.CommentDTO;
import ru.miraq.taskmanagementsystem.dto.comment.GetCommentDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskPriority;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;

import java.util.List;

@Builder
@Data
public class GetTaskDTO {

    private String name;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private String executor;

    private String author;

    private List<GetCommentDTO> commentDTO;
}
