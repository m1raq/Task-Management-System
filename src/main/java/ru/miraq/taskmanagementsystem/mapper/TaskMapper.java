package ru.miraq.taskmanagementsystem.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.miraq.taskmanagementsystem.dto.task.TaskDTO;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDto(TaskEntity task);

    TaskEntity toEntity(TaskDTO taskDTO);

    @AfterMapping
    default void afterMapping(TaskDTO taskDTO, @MappingTarget TaskEntity taskEntity) {
        if (taskEntity.getAuthor() != null) {
            UserDTO authorDTO = createAuthorDTO(taskEntity.getAuthor());
            taskDTO.setAuthor(authorDTO);
        }
        if (taskEntity.getExecutor() != null) {
            UserDTO executorDTO = createExecutorDTO(taskEntity.getExecutor());
            taskDTO.setExecutor(executorDTO);
        }
    }

    private UserDTO createAuthorDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private UserDTO createExecutorDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

}
