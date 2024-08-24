package ru.miraq.taskmanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.miraq.taskmanagementsystem.dto.ResponseMessageDTO;

@ControllerAdvice
public class RestAPIExceptionHandler {


    @ExceptionHandler(TaskNotFoundException.class)
    protected ResponseEntity<ResponseMessageDTO> handleTaskNotFoundException(TaskNotFoundException e) {
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message(e.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InputParamException.class)
    protected ResponseEntity<ResponseMessageDTO> handleInputParamException(InputParamException e) {
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message(e.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreateTaskException.class)
    protected ResponseEntity<ResponseMessageDTO> handleCreateTaskException(CreateTaskException e) {
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message(e.getMessage())
                .build(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(CredentialsPatternException.class)
    protected ResponseEntity<ResponseMessageDTO> handleCredentialsPatternException(CredentialsPatternException e) {
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message(e.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ResponseMessageDTO> handleUsernameNotFoundException() {
        return new ResponseEntity<>(ResponseMessageDTO.builder()
                .message("Указанный пользователь не найден")
                .build(), HttpStatus.NOT_FOUND);
    }


}
