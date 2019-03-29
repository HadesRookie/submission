package com.graduation.submission.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = -3096736268081409238L;

    private Integer id;

    private String username;

    private String password;

    private String email;

    private String payNum;

    private String realName;

    private String mobile;

    private Integer isUse;

    private Date insertTime;

    private Date updateTime;

    private Integer version;
}
