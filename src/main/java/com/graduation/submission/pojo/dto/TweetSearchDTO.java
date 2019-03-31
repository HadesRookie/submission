package com.graduation.submission.pojo.dto;

import lombok.Data;

/**
 * @ClassName TweetSearchDTO
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/31 21:36
 * @Version 1.0
 **/
@Data
public class TweetSearchDTO {

    private String author;

    private String title;

    private String tweetStatus;

    private String insertTimeStart;

    private String insertTimeEnd;
}
