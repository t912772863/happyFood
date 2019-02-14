package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.Dish;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface DishMapper {
    int insertSelective(Dish record);

    Dish queryById(@Param("id") String id);

    /**
     * 根据菜名和类型查询
     * @param name
     * @param type
     * @return
     */
    List<Dish> queryByNameAndType(@Param("name") String name, @Param("type") Integer type);

    /**
     * 查询某个类型所有不重复的菜名
     * @param type
     * @return
     */
    Set<String> queryAllDishNameByType(@Param("type") Integer type);
}