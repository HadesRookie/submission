package com.graduation.submission.pojo.dto;

import lombok.Data;

/**
 * @ClassName UserRoleDTO
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/18 21:02
 * @Version 1.0
 **/
@Data
public class UserRoleDTO {

    private Integer id;

    private String username;

    private String password;

    private String email;

    private String payNum;

    private String realName;

    private String mobile;

    private Integer isUse;

    private String insertTime;

    private String updateTime;

    private String roleNames;

    private Integer version;
}
