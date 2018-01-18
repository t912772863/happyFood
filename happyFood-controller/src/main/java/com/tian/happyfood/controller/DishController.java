package com.tian.happyfood.controller;

import com.tian.common.other.ResponseData;
import com.tian.common.validation.NotNull;
import com.tian.happyfood.service.IDishService;
import com.tian.happyfood.service.IDishVarietyService;
import com.tian.happyfood.service.dto.DishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.JMSException;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Controller
public class DishController extends BaseController{
    @Autowired
    private IDishVarietyService dishVarietyService;
    @Autowired
    private IDishService dishService;

    /**
     * 从京东万象上下载菜品分类
     * @return
     */
    @RequestMapping("downloadDishVariety")
    @ResponseBody
    public ResponseData downloadDishVariety(){
        dishVarietyService.getJDDishVariety();
        return success;
    }

    /**
     * 根据菜品名查询某个菜品
     * @param name
     * @return
     */
    @RequestMapping("queryDishByName")
    @ResponseBody
    public ResponseData queryDishByName(@NotNull String name) throws JMSException{
        return successData.setData(dishService.queryDetailByDishName(name));
    }

    /**
     * 跳转菜品详情页
     * @param id 菜品id
     * @return
     * @throws JMSException
     */
    @RequestMapping("f/toDishDetail")
    public ModelAndView toDishDetail(String id) throws JMSException {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("dishDetail");
        mav.addObject("id", id);
        return mav;
    }

    /**
     * 根据菜品ID查询菜品详情
     * @param id
     * @return
     */
    @RequestMapping("f/queryDishDetail")
    @ResponseBody
    public ResponseData queryDishDetail(String id){
        DishDto dishDto = dishService.queryDetailById(id);
        return successData.setData(dishDto);
    }





}
