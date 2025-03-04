package me.wjz.creeperhub.entity;

import lombok.Data;

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

    // 成功静态方法
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // 失败静态方法
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

}
