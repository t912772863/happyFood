package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.Message;

public interface MessageMapper {
    void insert(Message record);

    void insertSelective(Message record);
}