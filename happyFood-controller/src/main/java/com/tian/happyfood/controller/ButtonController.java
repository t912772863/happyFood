package com.tian.happyfood.controller;

import com.tian.common.other.PageParam;
import com.tian.common.other.ResponseData;
import com.tian.happyfood.dao.entity.Button;
import com.tian.happyfood.service.IButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
@Controller
public class ButtonController extends BaseController{
    @Autowired
    private IButtonService buttonService;

    /**
     * 插入一条按钮记录到本地
     * @param button
     * @return
     */
    @RequestMapping("insertButton")
    @ResponseBody
    public ResponseData insertButton(Button button){
        buttonService.insert(button);
        return success;
    }

    /**
     * 把本地的按钮设置到微信上
     * @return
     */
    @RequestMapping("uploadButtonToWX")
    @ResponseBody
    public ResponseData uploadButtonToWX(HttpServletRequest request) throws Exception {
        System.out.println(request.getHeader("x-forwarded-for"));
        System.out.println(request.getRemoteAddr());
        buttonService.uploadButtonOfWX();
        return success;
    }

    /**
     * 删除微信上的按钮
     * @return
     */
    @RequestMapping("deleteButtonWX")
    @ResponseBody
    public ResponseData deleteButtonWX(){
        return null;
    }


    @RequestMapping("queryButtonByPage")
    @ResponseBody
    public ResponseData queryButtonByPage(HttpServletRequest request, PageParam pageParam){
        pageParam.getParams().put("level", request.getParameter("level"));
        pageParam.getParams().put("search", request.getParameter("search"));

        pageParam.setResult(buttonService.queryByPage(pageParam));
        return successData.setData(pageParam);
    }

}
