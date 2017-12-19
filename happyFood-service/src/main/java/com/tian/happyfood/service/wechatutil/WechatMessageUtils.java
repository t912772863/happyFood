package com.tian.happyfood.service.wechatutil;

import com.tian.common.util.XmlUtils;
import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.service.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/19 0019.
 */
public class WechatMessageUtils {
    private static Logger logger = LoggerFactory.getLogger(WechatMessageUtils.class);

    public static Message parseXML(String xmlStr){
        MessageDTO messageDTO = new MessageDTO();
        try {
            XmlUtils.paserXmlToObject(xmlStr, messageDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解析收到的消息异常", e);
        }
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setFromUserName(messageDTO.getFromUserName());
        message.setToUserName(messageDTO.getToUserName());
        message.setMsgId(Long.parseLong(messageDTO.getMsgId()));
        message.setMsgCreateTime(new Date(Long.parseLong(messageDTO.getCreateTime())));
        message.setMsgType(messageDTO.getMsgType());
        return message;
    }
}
