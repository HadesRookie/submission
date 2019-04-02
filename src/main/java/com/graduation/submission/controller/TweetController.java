package com.graduation.submission.controller;

import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.common.TreeResult;
import com.graduation.submission.pojo.TweetCategory;
import com.graduation.submission.pojo.dto.TweetSearchDTO;
import com.graduation.submission.service.ManuscriptService;
import com.graduation.submission.service.TweetCategoryService;
import com.graduation.submission.utils.IStatusMessage;
import com.graduation.submission.utils.PageDataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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
@Slf4j
public class TweetController {

    @Autowired
    private TweetCategoryService tweetCategoryService;

    @Autowired
    private ManuscriptService manuscriptService;

    @RequestMapping("/tweetAdd")
    public String toTweetAdd(){
        return "/tweet/tweetAdd";
    }

    @RequestMapping("/tweetList")
    public String toTweetList(){
        return "/tweet/tweetList";
    }


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

    @RequestMapping(value = "/findCategory",method = RequestMethod.GET)
    @ResponseBody
    public List<TweetCategory> findCategory(){
        List<TweetCategory> tweetCategories = new ArrayList<>();
        try {
            tweetCategories = tweetCategoryService.findCategory();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  tweetCategories;
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

    /**
     * 获取推文列表
     * @param page
     * @param limit
     * @param tweetSearchDTO
     * @return
     */
    @RequestMapping(value = "/getTweetList",method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getTweetList(@RequestParam("page") Integer page,
                                        @RequestParam("limit") Integer limit, TweetSearchDTO tweetSearchDTO){
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            pdr = manuscriptService.getTweetList(page,limit,tweetSearchDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pdr;
    }

    @RequestMapping(value = "/getTweetListByNodeId",method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getTweetList(@RequestParam("page") Integer page,
                                       @RequestParam("limit") Integer limit, Integer nodeId){
        PageDataResult pdr = new PageDataResult();

        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            if (nodeId == null){
                pdr = null;
            }else {
                pdr = manuscriptService.getTweetListByNodeId(page,limit,nodeId);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return pdr;
    }


    /**
     * 获取分类下拉树数据
     * @return
     */
    @RequestMapping("/getTweetCategoryTree")
    @ResponseBody
    public TreeResult getTweetCategoryTree(){

        try {
            return tweetCategoryService.getCategoryTree();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 为推文添加类别id
     * @param id
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/addCategoryId",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult addCategoryId(Integer id,Integer categoryId){
        ResponseResult responseResult = new ResponseResult();
        try {
            responseResult = this.manuscriptService.addCategoryId(id,categoryId);
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return responseResult;
    }

    @RequestMapping(value = "/push",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult push(Integer id){
        ResponseResult responseResult = new ResponseResult();
        try {
            responseResult = this.manuscriptService.push(id);
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return responseResult;
    }

}
