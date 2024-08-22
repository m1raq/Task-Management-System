package ru.miraq.taskmanagementsystem.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.miraq.taskmanagementsystem.dto.task.TaskDTO;
import ru.miraq.taskmanagementsystem.entity.user.RoleType;

import java.util.List;


@Data
public class UserDTO {

    private Long id;

    private String email;

    private String password;

    private List<RoleType> role;

    private List<TaskDTO> task;

    private List<TaskDTO> taskInProgress;
}
