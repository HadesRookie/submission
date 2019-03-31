package com.graduation.submission.pojo;

import lombok.Data;

/**
 * @ClassName TweetCategoryChild
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/31 20:44
 * @Version 1.0
 **/
@Data
public class TweetCategoryChild {

    private Integer id;

    private String name;

    private Boolean open = false;

    private Boolean checked = true;
}
