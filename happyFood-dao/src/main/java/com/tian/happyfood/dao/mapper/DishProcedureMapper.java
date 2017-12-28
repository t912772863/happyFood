package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.DishProcedure;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DishProcedureMapper {
    int insertSelective(DishProcedure record);

    List<DishProcedure> queryByDishId(@Param("dishId")String dishId);
}