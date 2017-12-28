package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.DishMaterial;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface IDishMaterialService {
    void insert (DishMaterial dishMaterial);
    List<DishMaterial> queryByDishId(String dishId);

}
