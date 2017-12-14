package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.Test;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
public interface ITestService {
    void deleteByPrimaryKey(Long id);

    void insert(Test record);

    void insertSelective(Test record);

    Test selectByPrimaryKey(Long id);

    void updateByPrimaryKeySelective(Test record);

    void updateByPrimaryKey(Test record);
}
