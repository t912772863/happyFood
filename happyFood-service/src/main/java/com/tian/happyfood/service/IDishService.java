package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.Dish;
import com.tian.happyfood.service.dto.DishDto;
import com.tian.happyfood.service.jingdongutil.bean.JDDish;

import javax.jms.JMSException;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface IDishService {
    void insert(Dish dish);

    /**
     * 把从京东万象查询的数据按本地的格式入库
     * @param jdDishList
     */
    void insertDishDetail(List<JDDish> jdDishList);

    Dish queryById(String id);

    DishDto queryDetailById(String id);

    DishDto queryDetailByDishName(String dishName) throws JMSException;
}