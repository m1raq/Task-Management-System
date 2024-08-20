package ru.miraq.taskmanagementsystem.mapper;

import org.mapstruct.Mapper;
import ru.miraq.taskmanagementsystem.dto.TaskDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDto(TaskEntity task);

    TaskEntity toEntity(TaskDTO taskDTO);

}
