package com.graduation.submission.dao;

import com.graduation.submission.pojo.Manuscript;
import com.graduation.submission.pojo.dto.FinanceSearchDTO;
import com.graduation.submission.pojo.dto.ReviewSearchDTO;
import com.graduation.submission.pojo.dto.TweetSearchDTO;
import com.graduation.submission.pojo.vo.ManuscriptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ManuscriptMapper
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/26 22:52
 * @Version 1.0
 **/
@Mapper
@Component
public interface ManuscriptMapper {

    //添加文章
    int newsAdd(Manuscript manuscript);

    //获取所有自己投稿的信息
    List<ManuscriptVO> listManuscriptByUserId(Integer userId);

    //获取所有投稿的信息
    List<ManuscriptVO> getReviewList(@Param("reviewSearch")ReviewSearchDTO reviewSearch);

    //获取审核通过的所有推文信息
    List<ManuscriptVO> getTweetList(@Param("tweetSearch")TweetSearchDTO tweetSearch);

    //获取已推送的所有稿件信息
    List<ManuscriptVO> getFinanceList(@Param("financeSearch")FinanceSearchDTO financeSearch);

    //根据所选菜单节点获取推文信息
    List<ManuscriptVO> getTweetListByNodeId(Integer nodeId);

    //根据稿件id获取标题和内容
    ManuscriptVO getManuscriptById(Integer id);

    //删除稿件
    int deleteManuscript(Integer id);

    //编辑稿件
    int updateManuscript(@Param("topic")String topic,@Param("content")String content,@Param("mStatus")String mStatus,@Param("id")Integer id);

    //通过审核
    int reviewPass(@Param("mStatus")String mStatus,@Param("id")Integer id);

    //退回修改
    int returnEdit(@Param("content")String content,@Param("mStatus")String mStatus,@Param("id")Integer id);

    //添加类别id
    int addCategoryId(@Param("categoryId")Integer categoryId,@Param("id")Integer id);

    //推送
    int push(Integer id);

    //结算
    int settle(Integer id);
}
