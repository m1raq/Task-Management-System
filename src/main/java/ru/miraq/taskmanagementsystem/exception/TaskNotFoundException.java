package ru.miraq.taskmanagementsystem.exception;

public class TaskNotFoundException extends Exception{
    public TaskNotFoundException(){}
    public TaskNotFoundException(String message){
        super(message);
    }
}
