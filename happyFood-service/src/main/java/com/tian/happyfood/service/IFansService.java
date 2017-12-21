package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.Fans;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public interface IFansService {
    void insert(Fans record);

    void insertSelective(Fans record);
}
