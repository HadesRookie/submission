package com.graduation.submission.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Permission {

    private Integer id;

    private String permitName;

    private Integer pid;

    private Integer zIndex;

    private Integer permitType;

    private String description;

    private String permitCode;

    private String url;

    private Date insertTime;

    private Date updateTime;
}
