package ru.miraq.taskmanagementsystem.mapper;

import org.mapstruct.Mapper;
import ru.miraq.taskmanagementsystem.dto.UserDTO;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity userEntity);

    UserEntity toEntity(UserDTO userDTO);

}
