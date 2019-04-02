package com.graduation.submission.controller;

import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.pojo.dto.FinanceSearchDTO;
import com.graduation.submission.service.ManuscriptService;
import com.graduation.submission.utils.IStatusMessage;
import com.graduation.submission.utils.PageDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName FinanceController
 * @Description TODO
 * @Author LJL
 * @Date 2019/4/2 23:07
 * @Version 1.0
 **/
@Controller
@RequestMapping("/finance")
public class FinanceController {

    @Autowired
    private ManuscriptService manuscriptService;

    @RequestMapping("/financeSettle")
    public String toFinanceSettle(){
        return "/finance/financeSettle";
    }

    /**
     * 获取已推送的所以信息
     * @param page
     * @param limit
     * @param financeSearchDTO
     * @return
     */
    @RequestMapping(value = "/getFinanceList",method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getTweetList(@RequestParam("page") Integer page,
                                       @RequestParam("limit") Integer limit, FinanceSearchDTO financeSearchDTO){
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            pdr = manuscriptService.getFinanceList(page,limit,financeSearchDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pdr;
    }

    /**
     * 结算
     * @param id
     * @return
     */
    @RequestMapping(value = "/settle",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult push(Integer id){
        ResponseResult responseResult = new ResponseResult();
        try {
            responseResult = this.manuscriptService.settle(id);
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return responseResult;
    }
}
