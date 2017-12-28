package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.DishProcedure;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface IDishProcedureService {
    void insert (DishProcedure dishProcedure);
    List<DishProcedure> queryByDishId(String dishId);
}
