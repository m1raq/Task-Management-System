package ru.miraq.taskmanagementsystem.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {

    private Long id;

    @Schema(description = "Токен для аунтефикации при дальнейших запросах")
    private String token;

    @Schema(description = "Рефреш-токен для обновления jwt токена")
    private String refreshToken;

    private String username;

    private List<String> roles;


}
