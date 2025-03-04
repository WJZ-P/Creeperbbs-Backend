package me.wjz.creeperhub.entity;

import lombok.Data;
import me.wjz.creeperhub.constant.ErrorType;

import java.util.Map;

@Data
public class Result<T> {
    private int code;//状态码
    private String message;//消息
    private T data;//数据

    //构造数据
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ErrorType errorType, T data) {
        this.code = errorType.getCode();
        this.message = errorType.getMessage();
        this.data = data;
    }

    public Result(ErrorType errorType) {
        this.code = errorType.getCode();
        this.message = errorType.getMessage();
        this.data = null;
    }

    // 成功静态方法
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // 失败静态方法
    public static <T> Result<T> error(ErrorType errorType) {
        return new Result<>(errorType, null);
    }

    public static <T> Result<T> error(ErrorType errorType, T data) {
        return new Result<>(errorType, data);
    }

    public static Result<Void> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
