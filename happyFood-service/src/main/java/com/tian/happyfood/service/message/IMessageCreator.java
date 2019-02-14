package com.tian.happyfood.service.message;

import com.tian.happyfood.service.wechatutil.bean.message.SupperMessage;
import com.tian.happyfood.service.wechatutil.bean.response.WXResponseData;

import javax.jms.JMSException;

/**
 * 创建回复微信平台时的对象
 * Created by tianxiong on 2019/1/24.
 */
public interface IMessageCreator {
    /**
     * 根据收到的微信消息类型, 以及内容, 构建一个回复消息对象
     * @param reqMsg
     * @return
     */
    WXResponseData buildMsg(SupperMessage reqMsg) throws JMSException;
}
