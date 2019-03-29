package com.graduation.submission.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Role {

    private Integer id;

    private String roleName;

    private String code;

    private String description;

    private Date insertTime;

    private Date updateTime;
}
