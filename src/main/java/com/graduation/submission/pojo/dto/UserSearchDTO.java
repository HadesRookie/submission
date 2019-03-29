package com.graduation.submission.pojo.dto;

import lombok.Data;

/**
 * @ClassName UserSearchDTO
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/18 21:06
 * @Version 1.0
 **/
@Data
public class UserSearchDTO {

    private Integer page;

    private Integer limit;

    private String uname;

    private String umobile;

    private String insertTimeStart;

    private String insertTimeEnd;
}
