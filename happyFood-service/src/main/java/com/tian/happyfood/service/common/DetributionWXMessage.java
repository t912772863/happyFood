package com.tian.happyfood.service.common;

import com.tian.common.util.ActivemqUtils;
import com.tian.common.util.StrSimilarityUtils;
import com.tian.common.util.XmlUtils;
import com.tian.happyfood.dao.entity.*;
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
import com.tian.happyfood.service.wechatutil.bean.response.NewsResponse;
import com.tian.happyfood.service.wechatutil.bean.response.TextResponse;
import com.tian.happyfood.service.wechatutil.bean.wxuser.WXUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
            // 用户发过来的消息
            String userMsg = "text".equals(supperMessage.getMsgType()) ? message.getContent() : message.getRecognition();
            // 语音转换的消息会有标点, 这里过滤一下
            userMsg = userMsg.trim().replace("？", "").replace("，", "").replace("！", "").replace("。", "");
            String result = "";
            try {

                List<DishDto> dishDtoList = dishService.queryDetailByDishName(userMsg);
                // 如果没找到相应的菜品, 则生成提示语
                if (dishDtoList == null) {
                    // 为空, 则进行相似菜品推荐
                    String dishContent = getlikeDishName(userMsg);
                    // 回复一个菜品
                    TextResponse textResponse = new TextResponse();
                    textResponse.setFromUserName(supperMessage.getToUserName());
                    textResponse.setToUserName(supperMessage.getFromUserName());
                    textResponse.setMsgType("text");
                    textResponse.setCreateTime(System.currentTimeMillis() + "");
                    textResponse.setContent(dishContent);

                    result = XmlUtils.buildXml(textResponse, false, "xml");
                } else {
                    // 不为空, 则从众多做法中, 随机选一样返回(图文消息类型中的一个消息体)
                    NewsResponse.item item = executeDishDtoNews(dishDtoList);
                    List<NewsResponse.item> list = new ArrayList<NewsResponse.item>();
                    list.add(item);
                    NewsResponse newsResponse = new NewsResponse();
                    newsResponse.setFromUserName(supperMessage.getToUserName());
                    newsResponse.setToUserName(supperMessage.getFromUserName());
                    newsResponse.setMsgType("news");
                    newsResponse.setCreateTime(System.currentTimeMillis() + "");
                    newsResponse.setArticleCount("1");
                    newsResponse.setArticles(list);
                    newsResponse.setTitle(userMsg);
                    // 这个自己封装的方法不支嵌套的对象转xml, 另写个方法转换
                    result = buildNewsResponseToXml(newsResponse);
                }


            } catch (Exception e) {
                e.printStackTrace();
                logger.error("创建回复消息失败", e);
            }
            return result;
        }
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

    /**
     * 把查询出来的菜品详情. 转换成微信模版消息(文本类型的)
     *
     * @param dishDtoList
     * @return
     */
    private static String executeDishDtoText(List<DishDto> dishDtoList) {
        if (dishDtoList == null || dishDtoList.size() == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        // 同名菜, 取随机一种做法
        int index = new Random().nextInt(dishDtoList.size());
        DishDto dishDto = dishDtoList.get(index);

        stringBuilder = stringBuilder.append(dishDto.getName() + ": \n\n");
        stringBuilder.append("用料: \n");
        for (DishMaterial d : dishDto.getMaterials()) {
            stringBuilder.append(d.getName() + ": " + d.getAmount() + "\n");
        }

        stringBuilder.append("\n步骤: \n");
        int i = 1;
        for (DishProcedure d : dishDto.getProcedures()) {
            stringBuilder.append(i + ". " + d.getContent() + "\n");
            stringBuilder.append("参考图: " + d.getPic() + "\n");
            i++;
        }
        return stringBuilder.toString();
    }

    private NewsResponse.item executeDishDtoNews(List<DishDto> dishDtoList) {
        if (dishDtoList == null || dishDtoList.size() == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        // 同名菜, 取随机一种做法
        int index = new Random().nextInt(dishDtoList.size());
        DishDto dishDto = dishDtoList.get(index);
        NewsResponse.item item = new NewsResponse.item();
        item.setTitle(dishDto.getName());
        item.setDescription(dishDto.getContent());
        item.setPicUrl(dishDto.getPic());
        item.setUrl("http://118.126.115.206/happyFood/f/toDishDetail?id=" + dishDto.getId());
        return item;
    }

    /**
     * 从菜品名库中,进行匹配度在60%的查找, 最多找三个就不找了.
     *
     * @param dishName
     * @return
     */
    private static String getlikeDishName(String dishName) {
        String result = "不好意思呢, 暂时找不到您要的菜品, 要不, 您待会儿再过来看看  *_*||";

        String matchName = "";
        int i = 0;
        for (String s : SystemCache.dishNameSet) {
            if (s == null || "".equals(s.trim())) {
                continue;
            }
            logger.info("对比菜名匹配: " + s + ",  " + dishName);
            float index = StrSimilarityUtils.getSimilarityRatio(s, dishName);
            if (index >= 0.5) {
                matchName += s + "; ";
                i++;
                if (i == 3) {
                    break;
                }
            }
        }
        // 如果还是没找到相似的菜品名, 则给一条提示语
        if (!"".equals(matchName)) {
            result += "\n\n为您推荐相似菜品: " + matchName;
        }
        return result;
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
