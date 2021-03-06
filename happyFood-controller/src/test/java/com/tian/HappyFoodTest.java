package com.tian;

import com.alibaba.fastjson.JSONObject;
import com.tian.happyfood.dao.entity.Dish;
import com.tian.happyfood.service.IDishService;
import com.tian.happyfood.service.message.JDMessageCreator;
import com.tian.happyfood.service.message.WebMessageCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by tianxiong on 2019/1/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:*.xml"})
public class HappyFoodTest {
    @Autowired
    private WebMessageCreator webMessageCreator;
    @Autowired
    private JDMessageCreator jdMessageCreator;
    @Autowired
    private IDishService dishService;

    @Test
    public void test1(){
        String s = JDMessageCreator.getLikeDishName("清蒸鲈鱼");
        System.out.println(s);
    }

    @Test
    public void testGetDish(){
        String id = "103";
        Dish dish = dishService.queryById(id);
        System.out.println(JSONObject.toJSONString(dish));
    }

}
