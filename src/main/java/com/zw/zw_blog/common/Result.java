package com.zw.zw_blog.common;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(),data);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(ResultCode code) {
        return new Result<>(code.getCode(),code.getMsg(),null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code,msg,null);
    }
}
