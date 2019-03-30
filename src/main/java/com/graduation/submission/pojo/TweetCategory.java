package com.graduation.submission.pojo;

import lombok.Data;

/**
 * @Author: hades
 * @Date: 2019/3/30 11:12
 * @Version 1.0
 */
@Data
public class TweetCategory {

    private Integer id;

    private String category;

    private String description;

    private Integer parentId;

}
