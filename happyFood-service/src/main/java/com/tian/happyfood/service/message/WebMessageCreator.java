package com.tian.happyfood.service.message;

import com.tian.happyfood.dao.entity.Dish;
import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.service.IDishService;
import com.tian.happyfood.service.wechatutil.WXMessageUtils;
import com.tian.happyfood.service.wechatutil.bean.message.SupperMessage;
import com.tian.happyfood.service.wechatutil.bean.response.NewsResponse;
import com.tian.happyfood.service.wechatutil.bean.response.TextResponse;
import com.tian.happyfood.service.wechatutil.bean.response.WXResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.tian.happyfood.service.message.JDMessageCreator.getLikeDishName;

/**
 * web版本, 菜谱消息构建者
 * Created by tianxiong on 2019/1/24.
 */
@Component
public class WebMessageCreator implements IMessageCreator {
    @Autowired
    private IDishService dishService;

    public WXResponseData buildMsg(SupperMessage reqMsg) throws JMSException {
        WXResponseData responseData = null;
        Message message = new Message();
        WXMessageUtils.convertMessage(reqMsg, message);
        // 如果是文本消息,或者语音, 则查找菜品做法,其它类型消息先不回复
        // 用户发过来的消息
        String userMsg = "text".equals(reqMsg.getMsgType()) ? message.getContent() : message.getRecognition();
        // 语音转换的消息会有标点, 这里过滤一下
        userMsg = userMsg.trim().replace("？", "").replace("，", "").replace("！", "").replace("。", "");

        List<Dish> dishList = dishService.queryByDishName(userMsg, 2);
        // 如果没找到相应的菜品, 则生成提示语
        if (dishList == null || dishList.size() ==0) {
            // 为空, 则进行相似菜品推荐
            String dishContent = getLikeDishName(userMsg);
            // 回复一个菜品
            TextResponse textResponse = new TextResponse();
            textResponse.setFromUserName(reqMsg.getToUserName());
            textResponse.setToUserName(reqMsg.getFromUserName());
            textResponse.setMsgType("text");
            textResponse.setCreateTime(System.currentTimeMillis() + "");
            textResponse.setContent(dishContent);
            responseData = textResponse;

        } else {
            // 不为空, 则从众多做法中, 随机选一样返回(图文消息类型中的一个消息体)
            NewsResponse.item item = executeDishNews(dishList);
            List<NewsResponse.item> list = new ArrayList<NewsResponse.item>();
            list.add(item);
            NewsResponse newsResponse = new NewsResponse();
            newsResponse.setFromUserName(reqMsg.getToUserName());
            newsResponse.setToUserName(reqMsg.getFromUserName());
            newsResponse.setMsgType("news");
            newsResponse.setCreateTime(System.currentTimeMillis() + "");
            newsResponse.setArticleCount("1");
            newsResponse.setArticles(list);
            newsResponse.setTitle(userMsg);
            responseData = newsResponse;

        }
        return responseData;
    }

    private NewsResponse.item executeDishNews(List<Dish> dishList) {
        if (dishList == null || dishList.size() == 0) {
            return null;
        }
        // 同名菜, 取随机一种做法
        int index = new Random().nextInt(dishList.size());
        Dish dish = dishList.get(index);
        NewsResponse.item item = new NewsResponse.item();
        item.setTitle(dish.getName());
        item.setDescription(dish.getContent());
        item.setPicUrl(dish.getPic());
        item.setUrl("http://118.126.115.206/file/"+dish.getDetailUrl());
        return item;
    }


}
