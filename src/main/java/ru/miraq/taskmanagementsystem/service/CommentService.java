package ru.miraq.taskmanagementsystem.service;


public interface CommentService {

    void createComment(String taskName, String ownerEmail, String comment);

}
