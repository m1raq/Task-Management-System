package ru.miraq.taskmanagementsystem.service;

import ru.miraq.taskmanagementsystem.dto.user.UserDTO;


public interface UserService {

    UserDTO getUserByEmail(String email);

    boolean existByEmail(String email);

}
