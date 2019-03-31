package com.graduation.submission.service.impl;

import com.graduation.submission.common.TreeResult;
import com.graduation.submission.dao.TweetCategoryMapper;
import com.graduation.submission.pojo.TweetCategory;
import com.graduation.submission.pojo.TweetCategoryTree;
import com.graduation.submission.service.TweetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TweetCategoryServiceImpl
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/31 15:46
 * @Version 1.0
 **/
@Service
public class TweetCategoryServiceImpl implements TweetCategoryService {

    @Autowired
    private TweetCategoryMapper tweetCategoryMapper;

    @Override
    public List<TweetCategory> categoryList() {
        return this.tweetCategoryMapper.categoryList();
    }

    @Override
    public int insertCategory(TweetCategory tweetCategory) {
        return this.tweetCategoryMapper.insertCategory(tweetCategory);
    }

    @Override
    public TweetCategory getCategoryById(Integer id) {
        return this.tweetCategoryMapper.getCategoryById(id);
    }

    @Override
    public int updateCategory(TweetCategory tweetCategory) {
        return this.tweetCategoryMapper.updateCategory(tweetCategory);
    }

    @Override
    public String deleteCategory(Integer id) {
        List<TweetCategory> childCategory = this.tweetCategoryMapper.findChildCategory(id);
        if (null != childCategory && childCategory.size() > 0){
            return "删除失败，请您先删除该类别的子节点";
        }
        if (this.tweetCategoryMapper.deleteCategory(id) > 0){
            return "ok";
        }else {
            return "删除失败，请您稍后再试";
        }
    }

    @Override
    public TreeResult getCategoryTree() {
        TreeResult treeResult = new TreeResult();
        try {
            List<TweetCategoryTree> tweetCategoryTrees = this.tweetCategoryMapper.findCategoryTree();
            treeResult.setData(tweetCategoryTrees);
            treeResult.setCode(0);
            treeResult.setMsg("初始化成功");
        }catch (Exception e){
            e.printStackTrace();
            treeResult.setMsg("初始化失败");
        }


        return treeResult;
    }

}
