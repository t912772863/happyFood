package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.Event;
import com.tian.happyfood.dao.mapper.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
@Service
public class EventServiceImpl implements IEventService {
    @Autowired
    private EventMapper eventMapper;

    public void insert(Event event) {
        eventMapper.insert(event);
    }
}
