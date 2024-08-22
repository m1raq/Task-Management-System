package ru.miraq.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.miraq.taskmanagementsystem.dto.ResponseMessageDTO;
import ru.miraq.taskmanagementsystem.entity.user.RoleType;
import ru.miraq.taskmanagementsystem.exception.CredentialsPatternException;
import ru.miraq.taskmanagementsystem.security.dto.AuthRequestDTO;
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
            description = """
                    Введите адрес эл.почты и пароль.
                    
                    Варианты параметра role:\s
                    - executor - исполнитель
                    - customer - заказчик
                    """
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequestDTO authRequestDTO,
                                               @RequestParam RoleType role) {
        try {
            securityService.register(authRequestDTO, role);
            return new ResponseEntity<>(ResponseMessageDTO.builder()
                    .message("Регистрация прошла успешно")
                    .build(), HttpStatus.OK);
        } catch (CredentialsPatternException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @Operation(
            summary = "Логин",
            description = "Введите адрес эл.почты и пароль." +
                    " Из ответа использовать token для Bearer аунтефикации при дальнейших запросах"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        return new ResponseEntity<>(securityService.authenticateUser(authRequestDTO), HttpStatus.OK);
    }


    @Operation(
            summary = "Обновить токен"
    )
    @PutMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken
            (@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return new ResponseEntity<>(securityService.refreshToken(refreshTokenRequestDTO), HttpStatus.OK);
    }


}
