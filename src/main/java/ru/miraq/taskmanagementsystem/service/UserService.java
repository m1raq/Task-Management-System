package ru.miraq.taskmanagementsystem.service;

import ru.miraq.taskmanagementsystem.dto.UserDTO;

import java.util.List;

public interface UserService {

    void deleteUserByEmail(String email);

    void updateUser(UserDTO user);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();
}
