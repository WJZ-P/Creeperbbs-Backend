package me.wjz.creeperhub.exception;

import me.wjz.creeperhub.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result handleGlobalException(RuntimeException exception) {
        exception.printStackTrace();

        if (exception instanceof CreeperException)
            return ((CreeperException) exception).getResult();
        else
            return Result.error(500, exception.getMessage());
    }
}
