package com.zw.zw_blog.exception;


import com.zw.zw_blog.common.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;
    private  String msg;
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
