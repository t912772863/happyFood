package com.tian.happyfood.controller;

import com.tian.common.util.XmlUtils;
import com.tian.happyfood.dao.entity.Event;
import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.service.IEventService;
import com.tian.happyfood.service.IMessageService;
import com.tian.happyfood.service.wechatutil.WXMessageUtils;
import com.tian.happyfood.service.wechatutil.bean.WXRequestData;
import com.tian.happyfood.service.wechatutil.bean.event.SupperEvent;
import com.tian.happyfood.service.wechatutil.bean.message.SupperMessage;
import com.tian.happyfood.service.wechatutil.bean.response.TextResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分类处理微信推送过来的消息
 * Created by Administrator on 2017/12/21 0021.
 */
@Component
public class DetributionWXMessage {
    private static Logger logger = LoggerFactory.getLogger(DetributionWXMessage.class);
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IEventService eventService;

    public String executeWXMessage(WXRequestData requestData){
        String result = null;
        if(requestData instanceof SupperMessage){
            result = executeMessage((SupperMessage)requestData);
        }else if(requestData instanceof SupperEvent){
            result = executeEvent((SupperEvent)requestData);
        }
        return result;
    }

    private String executeMessage(SupperMessage supperMessage){
        Message message = new Message();
        WXMessageUtils.convertMessage(supperMessage, message);
        messageService.insert(message);
        // 所有类型消息先不回复
        return "";
    }

    private String executeEvent(SupperEvent supperEvent){
        Event event= new Event();
        WXMessageUtils.convertEvent(supperEvent, event);
        eventService.insert(event);
        // 如果是关注事件, 则回复一个欢迎语, 其它类型的事件先不回复
        if("subscribe".equals(event.getEvent())){
            TextResponse textResponse = new TextResponse();
            textResponse.setFromUserName(supperEvent.getToUserName());
            textResponse.setToUserName(supperEvent.getFromUserName());
            textResponse.setMsgType("text");
            textResponse.setCreateTime(System.currentTimeMillis()+"");
            textResponse.setContent("亲爱的小主, 我等您很久了^_^");
            String result = "";
            try{
                result = XmlUtils.buildXml(textResponse, false, "xml");
            }catch (Exception e){
                e.printStackTrace();
                logger.error("创建回复消息失败", e);
            }
            return result;
        }
        return "";
    }


}
