package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.Event;

public interface EventMapper {
    int insert(Event record);

    int insertSelective(Event record);
}