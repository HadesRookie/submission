package com.graduation.submission.controller;

import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.pojo.User;
import com.graduation.submission.pojo.vo.ManuscriptVO;
import com.graduation.submission.service.ManuscriptService;
import com.graduation.submission.utils.IStatusMessage;
import com.graduation.submission.utils.PageDataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SubmissionController
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/26 22:42
 * @Version 1.0
 **/
@Controller
@RequestMapping("/submit")
@Slf4j
public class SubmissionController {

    @Autowired
    private ManuscriptService manuscriptService;

    @RequestMapping("/submissionList")
    public String toSubmissionList() {
        return "/submit/submissionList";
    }

    @RequestMapping("/submissionAdd")
    public String toSubmissionAdd() {
        return "/submit/submissionAdd";
    }

    @RequestMapping(value = "/newsAdd",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult newsAdd(@RequestParam("topic") String topic,@RequestParam("content")String content){
        ResponseResult responseResult = new ResponseResult();
        try {
            responseResult = manuscriptService.newsAdd(topic,content);
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage(IStatusMessage.SystemStatus.ERROR.getMessage());
            return responseResult;
        }
        return responseResult;
    }

    /**
     * 分页查询自己发的所有稿件
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/newsList",method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getNewsList(@RequestParam("page") Integer page,
                                      @RequestParam("limit") Integer limit){
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            pdr = manuscriptService.listManuscriptByUserId(page,limit,existUser.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return pdr;
    }

    /**
     * 根据稿件id查询稿件消息
     * @param id
     * @return
     */
    @RequestMapping(value = "/getNewsDetail")
    @ResponseBody
    public Map<String,Object> getNewsDetail(@RequestParam("id")Integer id){
        Map<String,Object> map = new HashMap<>();
        try {
            ManuscriptVO manuscriptVO = manuscriptService.getManuscriptById(id);
            map.put("manuscript",manuscriptVO);
            map.put("msg","ok");
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg", "查询稿件错误，请您稍后再试");
        }
        return map;
    }

    @RequestMapping(value = "/newsEdit",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult newsEdit(@RequestParam("id")Integer id,@RequestParam("topic") String topic,
                                   @RequestParam("content")String content){
        ResponseResult responseResult = new ResponseResult();
        try {
            responseResult = manuscriptService.updateManuscript(topic,content,id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseResult;
    }

    @RequestMapping(value = "/delNews",method = RequestMethod.POST)
    @ResponseBody
    public String deleteNews(@RequestParam("id")Integer id){
        String msg = "";
        try {
            msg = manuscriptService.deleteManuscript(id);
        }catch (Exception e){
            e.printStackTrace();
            msg = "操作异常，请您稍后再试";
        }
        return msg;
    }

}
