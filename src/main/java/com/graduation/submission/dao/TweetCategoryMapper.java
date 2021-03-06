package com.graduation.submission.dao;

import com.graduation.submission.pojo.TweetCategory;
import com.graduation.submission.pojo.TweetCategoryTree;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: hades
 * @Date: 2019/3/30 11:14
 * @Version 1.0
 */
@Mapper
@Component
public interface TweetCategoryMapper {

    //添加分类
    int insertCategory(TweetCategory tweetCategory);

    //获取分类列表
    List<TweetCategory> categoryList();

    //根据类别id获取类别详细信息
    TweetCategory getCategoryById(Integer id);

    //更新类别
    int updateCategory(TweetCategory tweetCategory);

    //删除类别
    int deleteCategory(Integer id);

    //查找子节点
    List<TweetCategory> findChildCategory(Integer id);

    //获取所有类别 构建下拉树需要
    List<TweetCategoryTree> findCategoryTree();

}
