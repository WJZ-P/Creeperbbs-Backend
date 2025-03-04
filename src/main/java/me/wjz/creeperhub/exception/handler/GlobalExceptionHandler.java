package me.wjz.creeperhub.exception.handler;

import me.wjz.creeperhub.entity.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleGlobalException(RuntimeException exception) {
        exception.printStackTrace();
        return Result.error(500, exception.getMessage());
    }
}
