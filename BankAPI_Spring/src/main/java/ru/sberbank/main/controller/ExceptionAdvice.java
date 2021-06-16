package ru.sberbank.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.sberbank.main.modelDto.ServerResponseDTO;

@ControllerAdvice

public class ExceptionAdvice {

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ServerResponseDTO notFoundException(RuntimeException e) {
        return new ServerResponseDTO(e.getMessage());
    }
}