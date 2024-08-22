package ru.miraq.taskmanagementsystem.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.miraq.taskmanagementsystem.dto.task.TaskDTO;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity userEntity);

    UserEntity toEntity(UserDTO userDTO);

    @AfterMapping
    default void afterMapping(UserDTO userDTO, @MappingTarget UserEntity userEntity) {
        if (userEntity.getTask() != null) {
            userDTO.setTask(toDtoList(userEntity.getTask()));
        }
        if (userEntity.getTaskInProgress() != null) {
            userDTO.setTaskInProgress(toDtoList(userEntity.getTaskInProgress()));
        }
    }


    default List<TaskDTO> toDtoList(List<TaskEntity> list) {

        List<TaskDTO> list1 = new ArrayList<>();
        for (TaskEntity taskEntity : list) {
            list1.add(getTaskDTO(taskEntity));
        }
        return list1;
    }

    private static TaskDTO getTaskDTO(TaskEntity taskEntity) {

        UserDTO author = new UserDTO();
        author.setId(taskEntity.getAuthor().getId());
        author.setEmail(taskEntity.getAuthor().getEmail());
        author.setPassword(taskEntity.getAuthor().getPassword());

        TaskDTO taskDTO = new TaskDTO();

        if(taskEntity.getExecutor() != null) {
            UserDTO executor = new UserDTO();
            executor.setId(taskEntity.getExecutor().getId());
            executor.setEmail(taskEntity.getExecutor().getEmail());
            executor.setPassword(taskEntity.getExecutor().getPassword());
            taskDTO.setExecutor(executor);
        }
        taskDTO.setId(taskEntity.getId());
        taskDTO.setName(taskEntity.getName());
        taskDTO.setDescription(taskEntity.getDescription());
        taskDTO.setPriority(taskEntity.getPriority());
        taskDTO.setStatus(taskEntity.getStatus());
        taskDTO.setAuthor(author);
        return taskDTO;
    }


}
