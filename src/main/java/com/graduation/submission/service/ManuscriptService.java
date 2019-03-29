package com.graduation.submission.service;

import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.pojo.vo.ManuscriptVO;
import com.graduation.submission.utils.PageDataResult;

/**
 * @ClassName ManuscriptService
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/26 23:59
 * @Version 1.0
 **/
public interface ManuscriptService {

    /**
     * 投稿
     * @param topic
     * @param content
     * @return
     */
    ResponseResult newsAdd(String topic,String content);

    /**
     * 获取自己投稿的所以稿件消息
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    PageDataResult listManuscriptByUserId(int page,int limit,int userId);

    /**
     * 删除稿件
     * @param id
     * @return
     */
    String deleteManuscript(int id);

    /**
     * 根据id获取稿件消息
     * @param id
     * @return
     */
    ManuscriptVO getManuscriptById(int id);

    /**
     * 更新稿件
     * @param topic
     * @param content
     * @param id
     * @return
     */
    ResponseResult updateManuscript(String topic,String content,Integer id);


}
