package com.graduation.submission.pojo.vo;

import lombok.Data;

/**
 * @ClassName PermissionVO
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/18 21:11
 * @Version 1.0
 **/
@Data
public class PermissionVO {

    private Integer id;

    private String permitName;

    private Integer pid;

    private Integer zIndex;

    private Integer permitType;

    private String description;

    private String permitCode;

    private String url;

    private Boolean checked;

    private Boolean open;
}
