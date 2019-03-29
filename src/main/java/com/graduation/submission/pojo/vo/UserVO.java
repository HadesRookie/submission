package com.graduation.submission.pojo.vo;

import lombok.Data;

/**
 * @ClassName UserVO
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/24 18:00
 * @Version 1.0
 **/
@Data
public class UserVO {

    private String username;

    private String mobile;

    private String password;

    private String repass;

    private String code;

    private String smsCode;
}
