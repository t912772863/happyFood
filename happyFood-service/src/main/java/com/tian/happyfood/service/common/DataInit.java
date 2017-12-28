package com.tian.happyfood.service.common;

import com.tian.common.util.ActivemqUtils;
import com.tian.common.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;

/**
 * 这个类主要用于, 在spring初始化完成以后, 对于系统中要用到的一些数据的初始化, 或者数据检查等工作.
 * Created by Administrator on 2017/12/26 0026.
 */
@Component
public class DataInit {
    private static final Logger logger = LoggerFactory.getLogger(DataInit.class);

    @Value("${redis_ip}")
    private String redisIp;
    @Value("${redis_port}")
    private int redisPort;

    @PostConstruct
    public void init() throws JMSException {
        logger.info("====================> spring bean init over, data init start...");
        // 把配置文件初始化到Config中
        Config.init();

        // 初始化redis工具类
        JedisUtil.init(redisIp, redisPort);

        // 初始化mq的消费者
        ActivemqUtils.getQueueConsumerInstance(Config.config.getString("activemq_username"),
                Config.config.getString("activemq_password"),
                Config.config.getString("activemq_url"),
                Config.config.getString("activemq_destination"), new NewFansListener());

        // 初始化生产者
        DetributionWXMessage.init();

    }
}
