package com.tian.happyfood.controller;

import com.tian.common.other.ResponseData;
import com.tian.happyfood.dao.entity.Button;
import com.tian.happyfood.service.IButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 把一个本地的按钮设置到微信上
     * @param id
     * @return
     */
    public ResponseData uploadButtonToWX(Long id){
        return success;
    }

}
