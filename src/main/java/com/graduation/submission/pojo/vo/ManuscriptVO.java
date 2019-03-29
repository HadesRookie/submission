package com.graduation.submission.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: hades
 * @Date: 2019/3/28 11:49
 * @Version 1.0
 */
@Data
public class ManuscriptVO {

    private Integer id;

    private String username;

    private String topic;

    private String content;

    private String mStatus;

    private String tStatus;

    private String feeStatus;

    private Date insertTime;

    private Date updateTime;
}
