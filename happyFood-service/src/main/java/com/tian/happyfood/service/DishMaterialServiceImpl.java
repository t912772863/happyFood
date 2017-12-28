package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.DishMaterial;
import com.tian.happyfood.dao.mapper.DishMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Service
public class DishMaterialServiceImpl implements IDishMaterialService{
    @Autowired
    private DishMaterialMapper dishMaterialMapper;

    public void insert(DishMaterial dishMaterial) {
        dishMaterialMapper.insertSelective(dishMaterial);
    }

    public List<DishMaterial> queryByDishId(String dishId) {
        return dishMaterialMapper.queryByDishId(dishId);
    }



}
