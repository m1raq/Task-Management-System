package ru.miraq.taskmanagementsystem.dto.comment;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetCommentDTO {

    private String author;

    private String comment;

}
