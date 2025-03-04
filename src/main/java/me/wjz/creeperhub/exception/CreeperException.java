package me.wjz.creeperhub.exception;

import lombok.Getter;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Result;

@Getter
public class CreeperException extends RuntimeException {
    private final Result result;

    public CreeperException(Result result) {
        this.result = result;
    }
    public CreeperException(ErrorType errorType){
        this.result = Result.error(errorType.getCode(), errorType.getMessage());
    }
}
