package ru.miraq.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.miraq.taskmanagementsystem.dto.ResponseMessageDTO;
import ru.miraq.taskmanagementsystem.service.CommentService;
import ru.miraq.taskmanagementsystem.service.CommentServiceImpl;

@Tag(name = "Комментарии к задачам")
@RequestMapping("/comment")
@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Создание комментария")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAnyRole('EXECUTOR', 'CUSTOMER')")
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestParam String taskName,
                                                    @RequestParam String text,
                                                    Authentication authentication){
        commentService.createComment(taskName, authentication.getName(), text);
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message("Комментарий успешно создан")
                .build()
                ,HttpStatus.OK);

    }


}
