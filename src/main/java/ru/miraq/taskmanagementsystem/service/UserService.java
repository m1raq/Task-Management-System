package ru.miraq.taskmanagementsystem.service;

import ru.miraq.taskmanagementsystem.dto.user.GetUserDTO;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;

import java.util.List;


public interface UserService {

    UserDTO getUserByEmail(String email);

    boolean existByEmail(String email);

    List<GetUserDTO> getUsersBySortType(String sortType);

}
