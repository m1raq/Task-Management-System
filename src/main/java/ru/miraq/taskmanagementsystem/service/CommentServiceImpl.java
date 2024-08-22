package ru.miraq.taskmanagementsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.miraq.taskmanagementsystem.dto.comment.CommentDTO;
import ru.miraq.taskmanagementsystem.dto.task.TaskDTO;
import ru.miraq.taskmanagementsystem.mapper.CommentMapper;
import ru.miraq.taskmanagementsystem.repository.CommentRepository;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService{

    private final UserServiceImpl userService;

    private final CommentMapper commentMapper;

    private final TaskServiceImpl taskService;

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(UserServiceImpl userService, CommentMapper commentMapper, TaskServiceImpl taskService, CommentRepository commentRepository) {
        this.userService = userService;
        this.commentMapper = commentMapper;
        this.taskService = taskService;
        this.commentRepository = commentRepository;
    }

    @Override
    public void createComment(String taskName, String ownerEmail, String text) {
        CommentDTO commentDTO = new CommentDTO();
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(taskService.getTaskByName(taskName).getId());
        commentDTO.setTask(taskDTO);
        commentDTO.setAuthor(userService.getUserByEmail(ownerEmail));
        commentDTO.setText(text);
        commentRepository.save(commentMapper.toEntity(commentDTO));
        log.info("{} создал комментарий под задачей:{}", ownerEmail, taskName);
    }

}
