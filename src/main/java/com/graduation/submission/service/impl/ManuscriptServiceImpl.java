package com.graduation.submission.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.dao.ManuscriptMapper;
import com.graduation.submission.pojo.Manuscript;
import com.graduation.submission.pojo.User;
import com.graduation.submission.pojo.dto.ReviewSearchDTO;
import com.graduation.submission.pojo.vo.ManuscriptVO;
import com.graduation.submission.service.ManuscriptService;
import com.graduation.submission.utils.IStatusMessage;
import com.graduation.submission.utils.PageDataResult;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ManuscriptServiceImpl
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/27 0:01
 * @Version 1.0
 **/
@Service
public class ManuscriptServiceImpl implements ManuscriptService {

    @Autowired
    private ManuscriptMapper manuscriptMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseResult newsAdd(String topic, String content) {
        ResponseResult responseResult = new ResponseResult();
        User existUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (null == existUser) {
            responseResult.setCode(IStatusMessage.SystemStatus.NO_LOGIN
                    .getCode());
            responseResult.setMessage("您未登录或登录超时，请您重新登录后再试");
            return responseResult;
        }
        Manuscript manuscript = new Manuscript();
        manuscript.setUserId(existUser.getId());
        manuscript.setTopic(topic);
        manuscript.setContent(content);
        manuscript.setInsertTime(new Date());
        manuscript.setMStatus("等待审核");
        this.manuscriptMapper.newsAdd(manuscript);
        responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS
                .getCode());
        return responseResult;
    }

    @Override
    public PageDataResult listManuscriptByUserId(int page, int limit, int userId) {
        PageDataResult pdr = new PageDataResult();
        PageHelper.startPage(page, limit);
        List<ManuscriptVO> manuscriptVOS = manuscriptMapper.listManuscriptByUserId(userId);
        //获取分页查询后的数据
        PageInfo<ManuscriptVO> pageInfo = new PageInfo<>(manuscriptVOS);
        //设置获取到的总记录数total；
        pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
        pdr.setList(manuscriptVOS);
        return pdr;
    }

    @Override
    public PageDataResult getReviewList(int page, int limit, ReviewSearchDTO reviewSearchDTO) {
        PageDataResult pdr = new PageDataResult();
        PageHelper.startPage(page, limit);
        List<ManuscriptVO> manuscriptVOS = manuscriptMapper.getReviewList(reviewSearchDTO);
        //获取分页查询后的数据
        PageInfo<ManuscriptVO> pageInfo = new PageInfo<>(manuscriptVOS);
        //设置获取到的总记录数total；
        pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
        pdr.setList(manuscriptVOS);
        return pdr;
    }

    @Override
    public String deleteManuscript(int id) {
        return this.manuscriptMapper.deleteManuscript(id) == 1 ? "ok" : "删除失败，请您稍后再试";
    }

    @Override
    public ManuscriptVO getManuscriptById(int id) {
        return this.manuscriptMapper.getManuscriptById(id);
    }

    @Override
    public ResponseResult updateManuscript(String topic, String content, Integer id) {
        ResponseResult responseResult = new ResponseResult();

        int update = this.manuscriptMapper.updateManuscript(topic,content,"等待审核",id);
        if (update == 1){
            responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS
                    .getCode());
        }else {
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage(IStatusMessage.SystemStatus.ERROR.getMessage());
        }

        return responseResult;
    }

    @Override
    public ResponseResult reviewPass(Integer id) {
        ResponseResult responseResult = new ResponseResult();
        int pass = this.manuscriptMapper.reviewPass("审核通过",id);
        if (pass == 1){
            responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS
                    .getCode());
        }else {
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage(IStatusMessage.SystemStatus.ERROR.getMessage());
        }

        return responseResult;
    }

    @Override
    public ResponseResult returnEdit(String content, Integer id) {
        ResponseResult responseResult = new ResponseResult();
        int returnEdit = this.manuscriptMapper.returnEdit(content,"退回修改",id);
        if (returnEdit == 1){
            responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS
                    .getCode());
        }else {
            responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
            responseResult.setMessage(IStatusMessage.SystemStatus.ERROR.getMessage());
        }

        return responseResult;
    }
}
