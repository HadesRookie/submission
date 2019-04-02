package com.graduation.submission.service;

import com.graduation.submission.common.TreeResult;
import com.graduation.submission.pojo.TweetCategory;

import java.util.List;

/**
 * @ClassName TweetCategoryService
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/31 15:45
 * @Version 1.0
 **/
public interface TweetCategoryService {

    //获取所有分类
    List<TweetCategory> categoryList();

    List<TweetCategory> findCategory();

    //插入分类
    int  insertCategory(TweetCategory tweetCategory);

    //根据类别id获取类别详细信息
    TweetCategory getCategoryById(Integer id);

    //更新类别
    int updateCategory(TweetCategory tweetCategory);

    //删除类别
    String deleteCategory(Integer id);

    //返回下拉树所需数据
    TreeResult getCategoryTree();
}
