package ru.miraq.taskmanagementsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Task Management System",
                version = "1.0",
                description = "Система Управления Задачами. Позволяет управлять задачами и их выполнением.",
                contact = @Contact(
                        name = "Daniil Yagodkin",
                        email = "agodkidaniil@gmail.com",
                        url = "https://t.me/cfogoogle"
                )
        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
}
