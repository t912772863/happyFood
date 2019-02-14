package com.tian.happyfood.service.message;

import com.tian.common.util.StrSimilarityUtils;
import com.tian.happyfood.dao.entity.DishMaterial;
import com.tian.happyfood.dao.entity.DishProcedure;
import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.service.IDishService;
import com.tian.happyfood.service.common.SystemCache;
import com.tian.happyfood.service.dto.DishDto;
import com.tian.happyfood.service.wechatutil.WXMessageUtils;
import com.tian.happyfood.service.wechatutil.bean.message.SupperMessage;
import com.tian.happyfood.service.wechatutil.bean.response.NewsResponse;
import com.tian.happyfood.service.wechatutil.bean.response.TextResponse;
import com.tian.happyfood.service.wechatutil.bean.response.WXResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 京东万像, 菜品接口格式的菜品返回
 * Created by tianxiong on 2019/1/24.
 */
@Component
public class JDMessageCreator implements IMessageCreator {
    private static final Logger log = LoggerFactory.getLogger(JDMessageCreator.class);
    @Autowired
    private IDishService dishService;

    public WXResponseData buildMsg(SupperMessage reqMsg) {
        WXResponseData responseData = null;
        Message message = new Message();
        WXMessageUtils.convertMessage(reqMsg, message);
        // 如果是文本消息,或者语音, 则查找菜品做法,其它类型消息先不回复
        // 用户发过来的消息
        String userMsg = "text".equals(reqMsg.getMsgType()) ? message.getContent() : message.getRecognition();
        // 语音转换的消息会有标点, 这里过滤一下
        userMsg = userMsg.trim().replace("？", "").replace("，", "").replace("！", "").replace("。", "");

        List<DishDto> dishDtoList = null;
        try {
            dishDtoList = dishService.queryDetailByDishName(userMsg, 1);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        // 如果没找到相应的菜品, 则生成提示语
        if (dishDtoList == null) {
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
            NewsResponse.item item = executeDishDtoNews(dishDtoList);
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

    private NewsResponse.item executeDishDtoNews(List<DishDto> dishDtoList) {
        if (dishDtoList == null || dishDtoList.size() == 0) {
            return null;
        }
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
    public static String getLikeDishName(String dishName) {
        String result = "不好意思呢, 暂时找不到您要的菜品, 要不, 您待会儿再过来看看  *_*||";

        String matchName = "";
        int i = 0;
        for (String s : SystemCache.dishNameSet) {
            if (s == null || "".equals(s.trim())) {
                continue;
            }
            log.info("对比菜名匹配: " + s + ",  " + dishName);
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
}
