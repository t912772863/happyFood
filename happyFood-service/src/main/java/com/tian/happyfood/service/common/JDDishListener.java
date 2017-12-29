package com.tian.happyfood.service.common;

import com.tian.happyfood.service.IDishService;
import com.tian.happyfood.service.jingdongutil.JDDishUtils;
import com.tian.happyfood.service.jingdongutil.bean.JDDish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

/**
 * 通过关键字,到京东万象上查找相似菜品
 * Created by Administrator on 2017/12/28 0028.
 */
public class JDDishListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(NewFansListener.class);
    public void onMessage(Message message) {
        try {
            String s = ((TextMessage)message).getText();
           logger.info("收到消息: "+ s);
            // 根据关键字从京东万象获取数据
            List<JDDish> jdDishList = JDDishUtils.searchDish(s,20);
            // 把数据入库本地
            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            IDishService dishService = wac.getBean(IDishService.class);
            dishService.insertDishDetail(jdDishList);
            // 消息确认
            message.acknowledge();
        } catch (JMSException e) {
            logger.error("收到消息异常",e);
        }

    }
}
