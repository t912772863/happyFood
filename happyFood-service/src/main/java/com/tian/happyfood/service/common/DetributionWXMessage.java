package com.tian.happyfood.service.common;

import com.tian.common.util.ActivemqUtils;
import com.tian.common.util.XmlUtils;
import com.tian.happyfood.dao.entity.Event;
import com.tian.happyfood.dao.entity.Fans;
import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.service.IEventService;
import com.tian.happyfood.service.IFansService;
import com.tian.happyfood.service.IMessageService;
import com.tian.happyfood.service.message.IMessageCreator;
import com.tian.happyfood.service.message.MessageCreaterAdapter;
import com.tian.happyfood.service.wechatutil.WXMessageUtils;
import com.tian.happyfood.service.wechatutil.WXUserUtils;
import com.tian.happyfood.service.wechatutil.bean.WXRequestData;
import com.tian.happyfood.service.wechatutil.bean.event.SupperEvent;
import com.tian.happyfood.service.wechatutil.bean.message.SupperMessage;
import com.tian.happyfood.service.wechatutil.bean.response.NewsResponse;
import com.tian.happyfood.service.wechatutil.bean.response.TextResponse;
import com.tian.happyfood.service.wechatutil.bean.response.WXResponseData;
import com.tian.happyfood.service.wechatutil.bean.wxuser.WXUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private MessageCreaterAdapter messageCreaterAdapter;
    @Value("${dish_version}")
    private String dishVersion;

    private static ActivemqUtils.Producer producer;

    public static void init() throws JMSException {
        producer = ActivemqUtils.getQueueProducerInstance(Config.config.getString("activemq_username"),
                Config.config.getString("activemq_password"),
                Config.config.getString("activemq_url"),
                Config.config.getString("activemq_destination_fan"));
    }

    public String executeWXMessage(WXRequestData requestData) throws JMSException {
        String result = null;
        if (requestData instanceof SupperMessage) {
            result = executeMessage((SupperMessage) requestData);
        } else if (requestData instanceof SupperEvent) {
            result = executeEvent((SupperEvent) requestData);
        }
        return result;
    }

    private String executeMessage(SupperMessage supperMessage) {
        Message message = new Message();
        WXMessageUtils.convertMessage(supperMessage, message);
        // 微信平台,如果在短时间内没有收到回复, 会尝试至多三次消息重推送, 所以这里在插入之前要看看这个消息本地是否有了.
        Message localMessage = messageService.queryByMsgId(message.getMsgId());
        if (localMessage == null) {
            try {
                messageService.insert(message);
            } catch (Exception e) {
                // 高并发可能出现重复数据, 违反数据库唯一
                logger.error("插入消息到本地异常", e);
            }

        }
        // 如果是文本消息,或者语音, 则查找菜品做法,其它类型消息先不回复
        if ("text".equals(supperMessage.getMsgType()) || "voice".equals(supperMessage.getMsgType())) {
            String result = "";
            try {
                IMessageCreator messageCreator = messageCreaterAdapter.getCreator(dishVersion);
                WXResponseData wxResponseData = messageCreator.buildMsg(supperMessage);
                if(wxResponseData instanceof TextResponse){
                    result = XmlUtils.buildXml((TextResponse)wxResponseData, false, "xml");
                }else if(wxResponseData instanceof NewsResponse){
                    result = buildNewsResponseToXml((NewsResponse)wxResponseData);
                }else {
                    logger.error("unknown msg type.");
                }

            } catch (Exception e) {
                logger.error("创建回复消息失败", e);
            }
            return result;
        }
        // 其它类型的消息, 目前不支持,先不处理吧
        return "";
    }

    private String executeEvent(SupperEvent supperEvent) throws JMSException {
        Event event = new Event();
        WXMessageUtils.convertEvent(supperEvent, event);
        eventService.insert(event);
        // 如果是关注事件, 则回复一个欢迎语, 其它类型的事件先不回复, 并且把该用户的信息查询出来存入数据库
        if ("subscribe".equals(event.getEvent())) {
            // 把新关注用户的信息入库
            WXUser wxUser = WXUserUtils.getUserBuOpenId(event.getFromUserName());
            producer.sendText(event.getFromUserName());
            if (wxUser != null) {
                Fans fans = convertToFans(wxUser);
                fansService.insertSelective(fans);
            }


            // 回复一个欢迎词
            TextResponse textResponse = new TextResponse();
            textResponse.setFromUserName(supperEvent.getToUserName());
            textResponse.setToUserName(supperEvent.getFromUserName());
            textResponse.setMsgType("text");
            textResponse.setCreateTime(System.currentTimeMillis() + "");
            textResponse.setContent("亲爱的小主, 我等您很久了, 想吃点啥呢 ? 告诉我吧! \n^_^");
            String result = "";
            try {
                result = XmlUtils.buildXml(textResponse, false, "xml");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("创建回复消息失败", e);
            }
            return result;
        }
        return "";
    }

    /**
     * 把wxUser对象中的值转换到Fans中
     *
     * @param wxUser
     * @return
     */
    private static Fans convertToFans(WXUser wxUser) {
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

    private static String buildNewsResponseToXml(NewsResponse newsResponse) {
        try {
            List<NewsResponse.item> list = newsResponse.getArticles();
            StringBuilder sb = new StringBuilder("");
            for (NewsResponse.item i : list) {

                String s = XmlUtils.buildXml(i, false, "item");
                sb.append(s);

            }
            newsResponse.setArticles(null);
            String pStr = XmlUtils.buildXml(newsResponse, false, "xml");
            pStr = pStr.replace("</xml>","");
            pStr = pStr + "<Articles>"+sb.toString()+"</Articles>" + "</xml>";
            return pStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
