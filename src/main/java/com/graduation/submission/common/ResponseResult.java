package com.graduation.submission.common;

import com.graduation.submission.utils.IStatusMessage;

import java.io.Serializable;

/**
 * @ClassName ResponseResult
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/4 1:44
 * @Version 1.0
 **/
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 7285065610386199394L;

    private String code;
    private String message;
    private Object obj;

    public ResponseResult() {
        this.code = IStatusMessage.SystemStatus.SUCCESS.getCode();
        this.message = IStatusMessage.SystemStatus.SUCCESS.getMessage();
    }

    public ResponseResult(IStatusMessage statusMessage){
        this.code = statusMessage.getCode();
        this.message = statusMessage.getMessage();

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override public String toString() {
        return "ResponseResult{" + "code='" + code + '\'' + ", message='"
                + message + '\'' + ", obj=" + obj + '}';
    }
}

