package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.DishProcedure;
import com.tian.happyfood.dao.mapper.DishProcedureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Service
public class DishProcedureServiceImpl implements IDishProcedureService {
    @Autowired
    private DishProcedureMapper dishProcedureMapper;

    public void insert(DishProcedure dishProcedure) {
        dishProcedureMapper.insertSelective(dishProcedure);
    }

    public List<DishProcedure> queryByDishId(String dishId) {
        return dishProcedureMapper.queryByDishId(dishId);
    }
}
