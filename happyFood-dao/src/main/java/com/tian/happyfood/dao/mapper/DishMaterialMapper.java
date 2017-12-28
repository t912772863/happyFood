package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.DishMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DishMaterialMapper {
    int insertSelective(DishMaterial record);

    List<DishMaterial> queryByDishId(@Param("dishId")String dishId);
}