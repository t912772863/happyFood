package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.Dish;
import org.apache.ibatis.annotations.Param;

public interface DishMapper {
    int insertSelective(Dish record);

    Dish queryById(@Param("id") String id);

    Dish queryByName(@Param("name") String name);
}