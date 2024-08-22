package ru.miraq.taskmanagementsystem.service;

import ru.miraq.taskmanagementsystem.entity.task.CommentEntity;

public interface CommentService {

    void createComment(String taskName, String ownerEmail, String comment);

}
