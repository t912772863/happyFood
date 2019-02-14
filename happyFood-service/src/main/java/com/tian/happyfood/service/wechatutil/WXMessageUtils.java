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
public class WXMessageUtils extends WXUtils{
    private static Logger logger = LoggerFactory.getLogger(WXMessageUtils.class);


    public static WXRequestData parseXML(String xmlStr) throws Exception {
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
        message.setMsgCreateTime(new Date(Long.parseLong(supperMessage.getCreateTime()+"000")));

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
            message.setDescription(((VoiceMessage)supperMessage).getRecognition());
            message.setRecognition(((VoiceMessage) supperMessage).getRecognition());
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
        event.setMsgCreateTime(new Date(Long.parseLong(supperEvent.getCreateTime()+"000")));

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
