package com.graduation.submission.pojo.vo;

import com.graduation.submission.pojo.UserRole;
import lombok.Data;

import java.util.List;

/**
 * @ClassName UserRoleVO
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/18 21:15
 * @Version 1.0
 **/
@Data
public class UserRoleVO {
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

    private List<UserRole> userRoles;

    private Integer version;
}
