package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.Message;

/**
 * Created by Administrator on 2017/12/19 0019.
 */
public interface IMessageService {
    void insert(Message record);

    void insertSelective(Message record);

    Message queryByMsgId(Long msgId);
}
