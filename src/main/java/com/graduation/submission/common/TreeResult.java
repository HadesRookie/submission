package com.graduation.submission.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 实现树形下拉框需要返回的数据格式
 * @ClassName TreeResult
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/31 21:12
 * @Version 1.0
 **/
@Data
public class TreeResult implements Serializable {

    private static final long serialVersionUID = 7285065610386199394L;

    private Integer code;

    private String msg;

    private Object data;

}
