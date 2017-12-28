package com.tian.happyfood.service.common;

import com.tian.common.util.ActivemqUtils;
import com.tian.common.util.XmlUtils;
import com.tian.happyfood.dao.entity.DishProcedure;
import com.tian.happyfood.dao.entity.Event;
import com.tian.happyfood.dao.entity.Fans;
import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.service.IDishService;
import com.tian.happyfood.service.IEventService;
import com.tian.happyfood.service.IFansService;
import com.tian.happyfood.service.IMessageService;
import com.tian.happyfood.service.dto.DishDto;
import com.tian.happyfood.service.wechatutil.WXMessageUtils;
import com.tian.happyfood.service.wechatutil.WXUserUtils;
import com.tian.happyfood.service.wechatutil.bean.WXRequestData;
import com.tian.happyfood.service.wechatutil.bean.event.SupperEvent;
import com.tian.happyfood.service.wechatutil.bean.message.SupperMessage;
import com.tian.happyfood.service.wechatutil.bean.response.TextResponse;
import com.tian.happyfood.service.wechatutil.bean.wxuser.WXUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private IFansService fansService;
    @Autowired
    private IDishService dishService;

    private static ActivemqUtils.Producer producer;

    public static void init() throws JMSException{
        producer =  ActivemqUtils.getQueueProducerInstance(Config.config.getString("activemq_username"),
                Config.config.getString("activemq_password"),
                Config.config.getString("activemq_url"),
                Config.config.getString("activemq_destination_fan"));
    }

    public String executeWXMessage(WXRequestData requestData) throws JMSException {
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
        // 如果是文本消息, 则查找菜品做法,其它类型消息先不回复
        if("text".equals(supperMessage.getMsgType())){
            String result = "";
            try{
                DishDto dishDto = dishService.queryDetailByDishName(message.getContent());
                String dishContent = executeDishDto(dishDto);

                // 回复一个菜品
                TextResponse textResponse = new TextResponse();
                textResponse.setFromUserName(supperMessage.getToUserName());
                textResponse.setToUserName(supperMessage.getFromUserName());
                textResponse.setMsgType("text");
                textResponse.setCreateTime(System.currentTimeMillis()+"");
                textResponse.setContent(dishContent);

                result = XmlUtils.buildXml(textResponse, false, "xml");
            }catch (Exception e){
                e.printStackTrace();
                logger.error("创建回复消息失败", e);
            }
            return result;
        }
        return "";
    }

    private String executeEvent(SupperEvent supperEvent) throws JMSException {
        Event event= new Event();
        WXMessageUtils.convertEvent(supperEvent, event);
        eventService.insert(event);
        // 如果是关注事件, 则回复一个欢迎语, 其它类型的事件先不回复, 并且把该用户的信息查询出来存入数据库
        if("subscribe".equals(event.getEvent())){
            // 把新关注用户的信息入库
            WXUser wxUser = WXUserUtils.getUserBuOpenId(event.getFromUserName());
            producer.sendText(event.getFromUserName());
            if(wxUser != null){
                Fans fans = convertToFans(wxUser);
                fansService.insertSelective(fans);
            }


            // 回复一个欢迎词
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

    /**
     * 把wxUser对象中的值转换到Fans中
     * @param wxUser
     * @return
     */
    private static Fans convertToFans(WXUser wxUser){
        Fans fans = new Fans();
        fans.setSubscribeStatus(wxUser.getSubscribe());
        fans.setCity(wxUser.getCity());
        fans.setCountry(wxUser.getCountry());
        fans.setGroupId(wxUser.getGroupid());
        fans.setSex(wxUser.getSex());
        fans.setHeadImgUrl(wxUser.getHeadimgurl());
        fans.setOpenId(wxUser.getOpenid());
        fans.setNickname(wxUser.getNickname());
        fans.setLanguage(wxUser.getLanguage());
        fans.setProvince(wxUser.getProvince());
        fans.setRemark(wxUser.getRemark());
        fans.setUnionId(wxUser.getUnionid());
        fans.setSubscribeTime(new Date(wxUser.getSubscribe_time()));
        return fans;
    }

    /**
     * 把查询出来的菜品详情. 转换成微信模版消息
     * @param dishDto
     * @return
     */
    private static String executeDishDto(DishDto dishDto){
        if(dishDto == null){
            return "不好意思呢, 暂时找不到你要的菜品, 要不, 您待会儿再过来看看  *_*||";
        }
        StringBuilder stringBuilder = new StringBuilder(dishDto.getName()+"做法如下: ");
        List<DishProcedure> procedureList = dishDto.getProcedures();
        int i = 1;
        for (DishProcedure d:procedureList) {
            stringBuilder.append("</br>"+i+". "+d.getContent());
            i++;
        }
        return stringBuilder.toString();
    }

}
