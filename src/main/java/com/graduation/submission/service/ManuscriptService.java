package com.graduation.submission.service;

import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.pojo.dto.FinanceSearchDTO;
import com.graduation.submission.pojo.dto.ReviewSearchDTO;
import com.graduation.submission.pojo.dto.TweetSearchDTO;
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
     * 获取所有稿件信息进行审核管理
     * @param page
     * @param limit
     * @param reviewSearchDTO
     * @return
     */
    PageDataResult getReviewList(int page, int limit, ReviewSearchDTO reviewSearchDTO);

    /**
     * 获取所有审核通过的推文
     * @param page
     * @param limit
     * @param tweetSearchDTO
     * @return
     */
    PageDataResult getTweetList(int page, int limit, TweetSearchDTO tweetSearchDTO);

    /**
     * 获取所以已推送的稿件
     * @param page
     * @param limit
     * @param financeSearchDTO
     * @return
     */
    PageDataResult getFinanceList(int page, int limit, FinanceSearchDTO financeSearchDTO);

    /**
     * 根据树节点获取推文
     * @param page
     * @param limit
     * @param nodeId
     * @return
     */
    PageDataResult getTweetListByNodeId(int page, int limit,Integer nodeId);

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

    /**
     * 审核通过
     * @param id
     * @return
     */
    ResponseResult reviewPass(Integer id);

    /**
     * 退回修改
     * @param opinion
     * @param id
     * @return
     */
    ResponseResult returnEdit(String opinion,Integer id);

    /**
     * 添加类别id
     * @param id
     * @param categoryId
     * @return
     */
    ResponseResult addCategoryId(Integer id,Integer categoryId);

    /**
     * 推送
     * @param id
     * @return
     */
    ResponseResult push(Integer id);

    /**
     * 结算
     * @param id
     * @return
     */
    ResponseResult settle(Integer id);
}
