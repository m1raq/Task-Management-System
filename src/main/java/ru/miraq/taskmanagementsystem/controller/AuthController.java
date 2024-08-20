package ru.miraq.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.miraq.taskmanagementsystem.dto.UserDTO;
import ru.miraq.taskmanagementsystem.security.SecurityService;
import ru.miraq.taskmanagementsystem.security.dto.AuthResponseDTO;
import ru.miraq.taskmanagementsystem.security.dto.RefreshTokenRequestDTO;
import ru.miraq.taskmanagementsystem.security.dto.RefreshTokenResponseDTO;

@Tag(name = "Авторизация", description = "Эндпоинты для регистрации/логина")
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final SecurityService securityService;

    @Autowired
    public AuthController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "В тело при запросе необходимо ввести только email & password"
    )
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody UserDTO userDTO) {
        securityService.register(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Логин",
            description = "В тело при запросе необходимо ввести только email & password." +
                    " Из ответа использовать token для Bearer аунтефикации при дальнейших запросах"
    )
    @GetMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(securityService.authenticateUser(userDTO), HttpStatus.OK);
    }

    @Operation(
            summary = "Обновить токен"
    )
    @PutMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken
            (@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return new ResponseEntity<>(securityService.refreshToken(refreshTokenRequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {
        securityService.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
