package com.tian.happyfood.service.wechatutil;

import com.tian.common.other.BusinessException;
import com.tian.common.util.XmlUtils;
import com.tian.happyfood.dao.entity.Event;
import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.service.wechatutil.bean.WXRequestData;
import com.tian.happyfood.service.wechatutil.bean.event.*;
import com.tian.happyfood.service.wechatutil.bean.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/19 0019.
 */
public class WechatMessageUtils {
    private static Logger logger = LoggerFactory.getLogger(WechatMessageUtils.class);

    public static Message parseXML3(String xmlStr){
        // 先判断收到的消息的类型, 再转成对应的实体
        String msgType = XmlUtils.getNodeValue(xmlStr, "MsgType");
        SupperMessage messageDTO = null;
        Message message = new Message();
        try {
            if("text".equals(msgType)){
                messageDTO = new TextMessage();
                XmlUtils.paserXmlToObject(xmlStr, messageDTO);
                message.setContent(((TextMessage)messageDTO).getContent());
            }else if("image".equals(msgType)){
                messageDTO = new ImageMessage();
                XmlUtils.paserXmlToObject(xmlStr, messageDTO);
                message.setMediaId(((ImageMessage)messageDTO).getMediaId());
                message.setPicUrl(((ImageMessage)messageDTO).getPicUrl());
            }else if("voice".equals(msgType)){
                messageDTO = new VoiceMessage();
                XmlUtils.paserXmlToObject(xmlStr, messageDTO);
                message.setMediaId(((VoiceMessage)messageDTO).getMediaId());
                message.setFormat(((VoiceMessage)messageDTO).getFormat());
            }else if("video".equals(msgType) || "shortvideo".equals(msgType)){
                messageDTO = new VideoMessage();
                XmlUtils.paserXmlToObject(xmlStr, messageDTO);
                message.setMediaId(((VideoMessage)messageDTO).getMediaId());
                message.setThumbMediaId(((VideoMessage)messageDTO).getThumbMediaId());
            }else if("location".equals(msgType)){
                messageDTO = new LocationMessage();
                XmlUtils.paserXmlToObject(xmlStr, messageDTO);
                message.setLocationX(((LocationMessage)messageDTO).getLocation_X());
                message.setLocationY(((LocationMessage)messageDTO).getLocation_Y());
                message.setScale(Integer.parseInt(((LocationMessage)messageDTO).getScale()));
                message.setLabel(((LocationMessage)messageDTO).getLabel());
            }else if("link".equals(msgType)){
                messageDTO = new LinkMessage();
                XmlUtils.paserXmlToObject(xmlStr, messageDTO);
                message.setTitile(((LinkMessage)messageDTO).getTitle());
                message.setDescription(((LinkMessage)messageDTO).getDescription());
                message.setUrl(((LinkMessage)messageDTO).getUrl());
            }else {
                throw new BusinessException(500, "未知类型的消息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解析收到的消息异常", e);
        }
        // 不同类型的共有属性,统一赋值
        message.setFromUserName(messageDTO.getFromUserName());
        message.setToUserName(messageDTO.getToUserName());
        message.setMsgId(Long.parseLong(messageDTO.getMsgId()));
        message.setMsgCreateTime(new Date(Long.parseLong(messageDTO.getCreateTime())));
        message.setMsgType(messageDTO.getMsgType());
        return message;
    }

    public static WXRequestData parseXML2(String xmlStr) throws Exception {
        // 先判断收到的消息的类型, 再转成对应的实体
        String msgType = XmlUtils.getNodeValue(xmlStr, "MsgType");

        WXRequestData messageDTO = null;
        if("event".equals(msgType)){
            String event = XmlUtils.getNodeValue(xmlStr, "Event");
            if("subscribe".equals(event) || "unsubscribe".equals(event)){
                messageDTO = new SubscribeEvent();
            } else if ("SCAN".equals(event)) {
                messageDTO= new ScanEvent();
            }else if("LOCATION".equals(event)){
                messageDTO = new LocationEvent();
            }else if("CLICK".equals(event)){
                messageDTO = new ClickEvent();
            }else {
                throw new BusinessException(500, "未知类型的事件");
            }
        }else {
            if("text".equals(msgType)){
                messageDTO = new TextMessage();
            }else if("image".equals(msgType)){
                messageDTO = new ImageMessage();
            }else if("voice".equals(msgType)){
                messageDTO = new VoiceMessage();
            }else if("video".equals(msgType) ){
                messageDTO = new VideoMessage();
            }else if("shortvideo".equals(msgType)){
                messageDTO = new ShortVideoMessage();
            }else if("location".equals(msgType)){
                messageDTO = new LocationMessage();
                XmlUtils.paserXmlToObject(xmlStr, messageDTO);
            }else if("link".equals(msgType)){
                messageDTO = new LinkMessage();
                XmlUtils.paserXmlToObject(xmlStr, messageDTO);
            }else {
                throw new BusinessException(500, "未知类型的消息");
            }
        }
        XmlUtils.paserXmlToObject(xmlStr, messageDTO);

        return messageDTO;
    }

    public static void convertMessage(SupperMessage supperMessage, Message message){
        message.setFromUserName(supperMessage.getFromUserName());
        message.setToUserName(supperMessage.getToUserName());
        message.setMsgType(supperMessage.getMsgType());
        message.setMsgId(Long.parseLong(supperMessage.getMsgId()));

        if(supperMessage instanceof TextMessage){
            message.setContent(((TextMessage) supperMessage).getContent());
        }
        if(supperMessage instanceof ImageMessage){
            message.setMediaId(((ImageMessage) supperMessage).getMediaId());
            message.setPicUrl(((ImageMessage) supperMessage).getPicUrl());
        }
        if(supperMessage instanceof VoiceMessage){
            message.setMediaId(((VoiceMessage) supperMessage).getMediaId());
            message.setFormat(((VoiceMessage) supperMessage).getFormat());
        }
        if(supperMessage instanceof VideoMessage){
            message.setMediaId(((VideoMessage) supperMessage).getMediaId());
            message.setThumbMediaId(((VideoMessage) supperMessage).getThumbMediaId());
        }
        if(supperMessage instanceof ShortVideoMessage){
            message.setMediaId(((ShortVideoMessage) supperMessage).getMediaId());
            message.setThumbMediaId(((ShortVideoMessage) supperMessage).getThumbMediaId());
        }
        if(supperMessage instanceof LinkMessage){
            message.setTitile(((LinkMessage) supperMessage).getTitle());
            message.setUrl(((LinkMessage) supperMessage).getUrl());
            message.setDescription(((LinkMessage) supperMessage).getDescription());
        }
        if(supperMessage instanceof  LocationMessage){
            message.setLocationX(((LocationMessage) supperMessage).getLocation_X());
            message.setLocationY(((LocationMessage) supperMessage).getLocation_Y());
            message.setScale(Integer.parseInt(((LocationMessage) supperMessage).getScale()));
            message.setLabel(((LocationMessage) supperMessage).getLabel());
        }


    }
    public static void convertEvent(SupperEvent supperEvent, Event event){
        event.setFromUserName(supperEvent.getFromUserName());
        event.setToUserName(supperEvent.getToUserName());
        event.setMsgType(supperEvent.getMsgType());
        event.setEvent(supperEvent.getEvent());
        if(supperEvent instanceof SubscribeEvent){
            event.setEventKey(((SubscribeEvent) supperEvent).getEventKey());
            event.setTicket(((SubscribeEvent) supperEvent).getTicket());
        }
        if(supperEvent instanceof ScanEvent){
            event.setEventKey(((ScanEvent) supperEvent).getEventKey());
            event.setTicket(((ScanEvent) supperEvent).getTicket());
        }
        if(supperEvent instanceof LocationEvent){
            event.setLatitude(((LocationEvent) supperEvent).getLatitude());
            event.setLongitude(((LocationEvent) supperEvent).getLongitude());
            event.setPrecision(((LocationEvent) supperEvent).getPrecision());
        }
        if(supperEvent instanceof ClickEvent){
        }

    }

}
