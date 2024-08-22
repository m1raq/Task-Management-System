package ru.miraq.taskmanagementsystem.mapper;

import org.mapstruct.Mapper;
import ru.miraq.taskmanagementsystem.dto.comment.CommentDTO;
import ru.miraq.taskmanagementsystem.entity.task.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentEntity toEntity(CommentDTO comment);

    CommentDTO toDto(CommentEntity comment);

}
