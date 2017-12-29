package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.Dish;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface DishMapper {
    int insertSelective(Dish record);

    Dish queryById(@Param("id") String id);

    List<Dish> queryByName(@Param("name") String name);

    Set<String> queryAllDishName();
}