package ru.miraq.taskmanagementsystem.service;

import ru.miraq.taskmanagementsystem.exception.TaskNotFoundException;

public interface CommentService {

    void createComment(String taskName, String ownerEmail, String comment) throws TaskNotFoundException;

}
