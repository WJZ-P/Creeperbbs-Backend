package me.wjz.creeperhub.constant;

import lombok.Getter;

@Getter
public enum ErrorType {
    //用户层错误
    USER_NOT_FOUND(10001, "用户不存在"),
    USER_REGISTERED(10002, "该用户名已被注册"),
    CAPTCHA_REQUEST_LIMIT_EXCEEDED(10003, "验证码请求过快，请稍后再试！"),
    CAPTCHA_NOT_FOUND(10004, "验证码已过期或不存在"),
    CAPTCHA_INCORRECT(10005, "验证码错误"),
    USER_EMAIL_INCORRECT(10006, "邮箱格式不正确，必须使用QQ邮箱!"),
    USERNAME_INCORRECT( 10007, "用户名格式不正确，必须为3-16位常见汉字、字母、数字或下划线!"),
    PASSWORD_INCORRECT( 10008, "密码格式不正确，必须为6-16位字母或数字!");
    private final int code;
    private final String message;

    // 枚举的构造方法必须是 private 的
    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
