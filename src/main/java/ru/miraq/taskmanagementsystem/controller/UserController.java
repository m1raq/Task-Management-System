package ru.miraq.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.miraq.taskmanagementsystem.dto.user.GetUserDTO;
import ru.miraq.taskmanagementsystem.service.UserService;
import ru.miraq.taskmanagementsystem.service.UserServiceImpl;

import java.util.List;

@Tag(name = "Пользователи")
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = """
            Возвращает список всех пользователей в зависимости от сортировки.
          
            Варианты сортировки:\s
             - 0 - возвращает всех пользователей
             - 1 - возвращает всех исполнителей
             - 2 - возвращает всех заказачиков
            """)
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/get-all")
    public ResponseEntity<List<GetUserDTO>> getAllUsers(@RequestParam String sortType) {
        return new ResponseEntity<>(userService.getUsersBySortType(sortType), HttpStatus.OK);
    }

}
