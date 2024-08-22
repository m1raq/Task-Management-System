package ru.miraq.taskmanagementsystem.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseMessageDTO {
    private String message;
}
