package com.graduation.submission.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 稿件类
 * @ClassName ManuscriptService
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/26 22:47
 * @Version 1.0
 **/
@Data
public class Manuscript {

    private Integer id;

    private Integer userId;

    private String topic;

    private String content;

    private String mStatus;

    private String tStatus;

    private String feeStatus;

    private Integer tCategoryId;

    private Date insertTime;
}
