package com.graduation.submission.pojo;

import lombok.Data;

/**
 * @ClassName TweetCategoryTree
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/31 20:33
 * @Version 1.0
 **/
@Data
public class TweetCategoryTree {

    private String id;

    private String pId;

    private String name;

    private String open = "null";

    private String chkDisabled = "false";

}
