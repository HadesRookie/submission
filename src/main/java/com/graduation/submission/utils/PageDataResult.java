package com.graduation.submission.utils;

import java.util.List;

/**
 * @ClassName PageDataResult
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/17 17:09
 * @Version 1.0
 **/
public class PageDataResult {

    //总记录数量
    private Integer totals;
    //当前页数据列表
    private List<?> list;

    private Integer code=200;

    public PageDataResult() {
    }

    public PageDataResult( Integer totals,
                           List<?> list) {
        this.totals = totals;
        this.list = list;
    }

    public Integer getTotals() {
        return totals;
    }

    public void setTotals(Integer totals) {
        this.totals = totals;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override public String toString() {
        return "PageDataResult{" + "totals=" + totals + ", list=" + list
                + ", code=" + code + '}';
    }
}
