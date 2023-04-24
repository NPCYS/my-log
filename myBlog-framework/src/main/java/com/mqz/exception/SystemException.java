package com.mqz.exception;

import com.mqz.enums.AppHttpCodeEnum;

public class SystemException extends RuntimeException{

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        code=httpCodeEnum.getCode();
        msg=httpCodeEnum.getMsg();
    }
}
