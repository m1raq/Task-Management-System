package ru.miraq.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;
import ru.miraq.taskmanagementsystem.entity.user.RoleType;

import java.util.List;

@Schema(name = "Сущность пользователя")
@Data
public class UserDTO {

    private Long id;

    private String email;

    private String password;

    private List<RoleType> role;

    private List<TaskEntity> task;

    private List<TaskEntity> taskInProgress;
}
