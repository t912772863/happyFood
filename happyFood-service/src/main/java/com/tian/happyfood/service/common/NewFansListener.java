package com.tian.happyfood.service.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by Administrator on 2017/12/26 0026.
 */
public class NewFansListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(NewFansListener.class);
    public void onMessage(Message message) {
        try {
            String s = ((TextMessage)message).getText();
            logger.info("收到消息: "+ s);
            // 消息确认
            message.acknowledge();
        } catch (JMSException e) {
            logger.error("收到消息异常",e);
        }

    }
}
