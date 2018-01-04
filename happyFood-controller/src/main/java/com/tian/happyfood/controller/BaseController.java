package com.tian.happyfood.controller;

import com.tian.common.other.ResponseData;
import com.tian.happyfood.dao.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
public class BaseController {
    /**
     * 成功返回实例:无业务数据
     */
    protected ResponseData success = ResponseData.successData;
    /**
     * 失败返回实例: 无业务数据
     */
    protected ResponseData failed = ResponseData.failedData;

    /**
     * 成功返回,调用set方法加入业务数据
     */
    protected ResponseData successData = new ResponseData(200,"success");

    /**
     * 失败返回, 调用setData方法加入业务数据.该对应为统一返回异常,如果要自定义code和message等则new一个
     */
    protected ResponseData failedData = new ResponseData(500,"failed");

    protected User getSessionUser(HttpServletRequest request){
        return (User)request.getSession(true).getAttribute("user");
    }
}
