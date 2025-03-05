package me.wjz.creeperhub.entity;

import lombok.Data;
import me.wjz.creeperhub.constant.ErrorType;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

@Data
public class Result<T> {
    private HttpStatus status;//状态码
    private int internalCode;//内部状态码
    private String message;//消息
    private T data;//数据

    //构造数据
    public Result(HttpStatus status, int internalCode, String message, T data) {
        this.internalCode = internalCode;
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public Result(ErrorType errorType, T data) {
        this.internalCode = errorType.getCode();
        this.message = errorType.getMessage();
        this.data = data;
        this.status = errorType.getStatus();
    }

    public Result(ErrorType errorType) {
        this.internalCode = errorType.getCode();
        this.message = errorType.getMessage();
        this.data = null;
        this.status = errorType.getStatus();
    }

    // 成功静态方法
    public static Result success(String message, Object data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("internalCode", 0);
        map.put("message", message);
        map.put("data", data);
        return new Result<>(HttpStatus.OK, 0, message, map);
    }

    // 失败静态方法
    public static Result<?> error(ErrorType errorType) {
        //传参没有body，那就把errorType的参数变为body
        HashMap<String, Object> map = new HashMap<>();
        map.put("internalCode", errorType.getCode());
        map.put("message", errorType.getMessage());
        return new Result<>(errorType, map);
    }

    public static <T> Result<T> error(ErrorType errorType, T data) {
        if (data instanceof HashMap) {
            HashMap<String, Object> map = (HashMap<String, Object>) data;
            map.put("internalCode", errorType.getCode());
            map.put("message", errorType.getMessage());
            return new Result<>(errorType, data);
        }
        return new Result<>(errorType, data);
    }

}
