package com.graduation.submission.controller;

import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.pojo.dto.ReviewSearchDTO;
import com.graduation.submission.service.ManuscriptService;
import com.graduation.submission.utils.PageDataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: hades
 * @Date: 2019/3/29 15:06
 * @Version 1.0
 */
@Controller
@RequestMapping("/review")
@Slf4j
public class ReviewController {

    @Autowired
    private ManuscriptService manuscriptService;

    @RequestMapping("/reviewList")
    public String toReviewList(){
        return "/review/reviewList";
    }

    @RequestMapping(value = "/getReviewList",method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getReviewList(@RequestParam("page") Integer page,
                                        @RequestParam("limit") Integer limit, ReviewSearchDTO reviewSearch){
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            pdr = manuscriptService.getReviewList(page,limit,reviewSearch);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pdr;
    }

    @RequestMapping(value = "/pass",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult reviewPass(@RequestParam("id")Integer id){
        ResponseResult responseResult = new ResponseResult();
        try {
            responseResult = manuscriptService.reviewPass(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseResult;
    }

    @RequestMapping(value = "/returnEdit",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult returnEdit(@RequestParam("opinion")String opinion,@RequestParam("id")Integer id){
        ResponseResult responseResult = new ResponseResult();
        try {
            responseResult = manuscriptService.returnEdit(opinion,id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseResult;
    }
}
