package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.Test;

public interface TestMapper {
    void deleteByPrimaryKey(Long id);

    void insert(Test record);

    void insertSelective(Test record);

    Test selectByPrimaryKey(Long id);

    void updateByPrimaryKeySelective(Test record);

    void updateByPrimaryKey(Test record);
}