package com.graduation.submission.pojo.dto;

import lombok.Data;

/**
 * 审核列表搜索类
 * @Author: hades
 * @Date: 2019/3/29 15:02
 * @Version 1.0
 */
@Data
public class ReviewSearchDTO {

    private String author;

    private String title;

    private String reviewStatus;

    private String insertTimeStart;

    private String insertTimeEnd;
}
