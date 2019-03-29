package com.graduation.submission.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName HttpResult
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/17 17:07
 * @Version 1.0
 **/
public class HttpResult {

    // 响应状态码
    private Integer code;

    // 响应体
    private String body;

    public HttpResult() {

    }

    public HttpResult(Integer code, String body) {
        super();
        this.code = code;
        if (StringUtils.isNotEmpty(body)) {
            this.body = body;
        }

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResult [code=" + code + ", body=" + body + "]";
    }
}
