package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.Fans;

public interface FansMapper {
    void insert(Fans record);

    void insertSelective(Fans record);
}