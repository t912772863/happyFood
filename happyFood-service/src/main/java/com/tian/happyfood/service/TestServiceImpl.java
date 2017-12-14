package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.Test;
import com.tian.happyfood.dao.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
@Service
public class TestServiceImpl implements ITestService{
    @Autowired
    private TestMapper testMapper;

    public void deleteByPrimaryKey(Long id) {
        testMapper.deleteByPrimaryKey(id);
    }

    public void insert(Test record) {
        testMapper.insert(record);
    }

    public void insertSelective(Test record) {
        testMapper.insertSelective(record);
    }

    public Test selectByPrimaryKey(Long id) {
        return testMapper.selectByPrimaryKey(id);
    }

    public void updateByPrimaryKeySelective(Test record) {
        testMapper.updateByPrimaryKeySelective(record);
    }

    public void updateByPrimaryKey(Test record) {
        testMapper.updateByPrimaryKey(record);
    }
}
