package com.tian.happyfood.dao.mapper;

import com.tian.happyfood.dao.entity.Message;

public interface MessageMapper {
    int insert(Message record);

    int insertSelective(Message record);

    Message queryByMsgId(Long msgId);
}