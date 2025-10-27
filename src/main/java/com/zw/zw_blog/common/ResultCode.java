package com.zw.zw_blog.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    // 成功
    SUCCESS(200, "操作成功"),

    // --- 用户错误 (对应 ERRORCODE.USER) ---
    USER_NOT_EXIST(10001, "用户不存在"),
    USER_ALREADY_EXIST(10002, "用户已存在"),
    USER_LOGIN_ERROR(10003, "用户名或密码错误"),

    // --- Token 错误 (对应 ERRORCODE.AUTHTOKEN) ---
    TOKEN_INVALID(10101, "无效的token"),
    TOKEN_EXPIRED(10102, "token已过期"),

    // --- 权限错误 (对应 ERRORCODE.AUTH) ---
    NO_PERMISSION(10201, "您没有权限访问"),

    // --- 其他错误 ---
    PARAM_VALIDATE_FAILED(50001, "参数校验失败"),
    INTERNAL_SERVER_ERROR(50000, "服务器内部错误");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
