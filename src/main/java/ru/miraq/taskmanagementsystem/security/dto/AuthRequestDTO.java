package ru.miraq.taskmanagementsystem.security.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {

    private String email;

    private String password;

}
