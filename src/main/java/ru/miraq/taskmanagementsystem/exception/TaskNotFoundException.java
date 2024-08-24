package ru.miraq.taskmanagementsystem.exception;

import java.util.function.Supplier;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(String message){
        super(message);
    }
}
