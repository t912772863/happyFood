package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.dao.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/19 0019.
 */
@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private MessageMapper messageMapper;

    public void insert(Message message) {
        message.setCreateTime(new Date());
        message.setStatus(1);
        messageMapper.insert(message);
    }

    public void insertSelective(Message record) {
    }
}
