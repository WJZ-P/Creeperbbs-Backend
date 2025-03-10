package me.wjz.creeperhub.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    //用户层错误
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 10001, "用户不存在"),
    USER_REGISTERED(HttpStatus.CONFLICT, 10002, "该用户名已被注册"),
    CAPTCHA_REQUEST_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, 10003, "验证码请求过快，请稍后再试！"),
    CAPTCHA_NOT_FOUND(HttpStatus.GONE, 10004, "验证码已过期或不存在"),
    CAPTCHA_INCORRECT(HttpStatus.BAD_REQUEST, 10005, "验证码错误"),
    USER_EMAIL_INCORRECT(HttpStatus.BAD_REQUEST, 10006, "邮箱格式不正确，必须使用QQ邮箱!"),
    USERNAME_INCORRECT(HttpStatus.BAD_REQUEST, 10007, "用户名格式不正确，必须为3-16位常见汉字、字母、数字或下划线!"),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, 10008, "密码格式不正确，必须为6-16位字母或数字!"),
    LOGIN_ATTEMPT_EXCEED(HttpStatus.TOO_MANY_REQUESTS, 10009, "登录尝试次数过多，请稍后再试!"),
    LOGIN_PARAMS_ERROR(HttpStatus.UNAUTHORIZED, 10010, "用户名或密码错误!"),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, 10011, "JWT已过期"),
    UN_LOGIN(HttpStatus.UNAUTHORIZED, 10013, "你还未登录!"),
    USER_EMAIL_REGISTERED(HttpStatus.CONFLICT, 10014, "该邮箱已被注册"),
    TOKEN_ERROR(HttpStatus.UNAUTHORIZED, 10015, "Token错误"),
    //系统层错误
    JWT_PARSE_ERROR(HttpStatus.BAD_REQUEST, 10012, "JWT解析错误"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 55555, "未知错误"),

    TARGET_TOKEN_ERROR(HttpStatus.BAD_REQUEST, 10016, "目标Token错误"),
    TOKEN_UNMATCHED(HttpStatus.BAD_REQUEST, 10017, "Token不匹配"),
    ORIGINAL_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, 10018, "原密码错误"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, 10019, "请求次数过多，请稍后再试!"),
    MAX_ITEMS_PER_PAGE_EXCEEDED(HttpStatus.BAD_REQUEST, 10020, "每页最多只能显示50条数据!"),
    PARAMS_ERROR(HttpStatus.BAD_REQUEST, 10021, "参数错误!");
    private final HttpStatus status; //  状态码枚举，类型为 HttpStatus
    private final int code;
    private final String message;

    // 枚举的构造方法必须是 private 的
    ErrorType(HttpStatus status, int code, String message) { //  构造方法参数顺序调整：HttpStatus status 在最前面
        this.status = status;
        this.code = code;
        this.message = message;
    }
}