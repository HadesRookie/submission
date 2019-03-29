package com.graduation.submission.pojo.vo;

import com.graduation.submission.pojo.RolePermission;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName RoleVO
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/18 21:14
 * @Version 1.0
 **/
@Data
public class RoleVO {

    private Integer id;

    private String roleName;

    private String code;

    private String description;

    private Date insertTime;

    private List<RolePermission> rolePermissions;
}
