package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.Fans;
import com.tian.happyfood.dao.mapper.FansMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
@Service
public class FansServiceImpl implements IFansService {
    @Autowired
    private FansMapper fansMapper;

    public void insert(Fans record) {

    }

    public void insertSelective(Fans fans) {
        fans.setSubscribeStatus(1);
        fansMapper.insertSelective(fans);
    }
}
