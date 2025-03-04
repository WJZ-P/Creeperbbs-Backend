package me.wjz.creeperhub.constant;

import lombok.Getter;

@Getter
public enum ErrorType {
    //用户层错误
    USER_NOT_FOUND(10001, "用户不存在"),
    USER_REGISTERED(10002, "该用户名已被注册"),
    CAPTCHA_REQUEST_LIMIT_EXCEEDED(10003, "验证码请求过快，请稍后再试！"),
    CAPTCHA_NOT_FOUND(10004, "验证已过期或不存在"),
    CAPTCHA_INCORRECT(10005, "验证码错误"),


    ;
    private final int code;
    private final String message;

    // 枚举的构造方法必须是 private 的
    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
