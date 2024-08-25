package ru.miraq.taskmanagementsystem.dto.user;

import lombok.Builder;
import lombok.Data;
import ru.miraq.taskmanagementsystem.entity.user.RoleType;

import java.util.List;

@Builder
@Data
public class GetUserDTO {

    private String email;

    private List<RoleType> role;

}
