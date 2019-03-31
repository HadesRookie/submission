package com.graduation.submission.controller;

import com.graduation.submission.pojo.TweetCategory;
import com.graduation.submission.service.TweetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @ClassName TweetController
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/31 15:44
 * @Version 1.0
 **/
@Controller
@RequestMapping("/tweet")
public class TweetController {

    @Autowired
    private TweetCategoryService tweetCategoryService;

    /**
     * 获取分类列表
     * @return
     */
    @RequestMapping(value = "/tweetCategory",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView categoryList(){
        ModelAndView mav = new ModelAndView("/tweet/tweetCategory");
        try {
            List<TweetCategory> categoryList = tweetCategoryService.categoryList();
            mav.addObject("categoryList",categoryList);
            mav.addObject("msg","ok");
        }catch (Exception e){
            e.printStackTrace();
        }
        return mav;
    }

    /**
     * 添加与更新分类
     * @param type [0：更新；1：新增子节点类别]
     * @param tweetCategory
     * @return
     */
    @RequestMapping(value = "/setCategory",method = RequestMethod.POST)
    @ResponseBody
    public String setCategory(@RequestParam("type")int type,TweetCategory tweetCategory){
        try {
            if(null != tweetCategory){
                if (type == 0){
                    this.tweetCategoryService.updateCategory(tweetCategory);
                }else if (type == 1){
                    tweetCategoryService.insertCategory(tweetCategory);
                }
                return "ok";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "设置分类出错，请您稍后再试";
    }

    /**
     * 获取类别 根据类别id
     * @param id
     * @return
     */
    @RequestMapping(value = "/getCategory",method = RequestMethod.GET)
    @ResponseBody
    public TweetCategory getCategory(@RequestParam("id")int id){
        try {
            if (id > 0){
                TweetCategory tweetCategory = this.tweetCategoryService.getCategoryById(id);
                return tweetCategory;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public String del(@RequestParam("id") int id) {
        try {
            if (id > 0){
                return this.tweetCategoryService.deleteCategory(id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "删除类别出错，请您稍后再试";
    }

}
