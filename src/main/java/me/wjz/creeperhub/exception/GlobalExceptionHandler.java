package me.wjz.creeperhub.exception;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public CreeperResponseEntity handleGlobalException(RuntimeException exception) {
        exception.printStackTrace();

        if (exception instanceof CreeperException)
            return new CreeperResponseEntity(((CreeperException) exception).getResult());
        else
            return new CreeperResponseEntity(Result.error(ErrorType.UNKNOWN_ERROR));
    }
}
