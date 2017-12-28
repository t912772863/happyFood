package com.tian.happyfood.service.wechatutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tian.common.other.BusinessException;
import com.tian.common.util.HttpUtils;
import com.tian.happyfood.service.dto.ButtonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15 0015.
 */
public class WXButtonUtils extends WXUtils{
    /**
     * 日志工具
     */
    private static Logger logger = LoggerFactory.getLogger(WXButtonUtils.class);


    /**
     * 上传按钮菜单的接口地址
     */
    private static final String UPLOAD_BUTTONS_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
    private static final String QUERY_BUTTONS_RUL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";

    public static boolean uploadButtons(List<ButtonDto> buttonDTOList) throws Exception {
        filterFiled(buttonDTOList);
        String buttonStr = JSONObject.toJSONString(buttonDTOList);
        // TODO: 2017/12/15 0015 这里的字符串还要处理成对应的格式
        buttonStr = "{ \"button\":" + buttonStr +"}";
        // 获取微信的access_token
        String accessToken = getWXAccessToken();
        if(accessToken == null){
            throw new BusinessException(500, "获取微信access_token异常");
        }
        String result = HttpUtils.doPostOfJson(UPLOAD_BUTTONS_URL+accessToken, buttonStr);
        logger.info("调用微信上传按钮接口返回: "+result);
        JSONObject jsonObject = JSON.parseObject(result);
        Integer errcode = jsonObject.getInteger("errcode");
        if(errcode != null && errcode != 0){
            logger.error("获取微信access_token异常");
        }
        return errcode == 0;
    }

    public static boolean deleteButton(){
        return false;
    }

    /**
     * 过滤字段
     * @param buttonDTOList
     */
    private static void filterFiled (List<ButtonDto> buttonDTOList){
        if(buttonDTOList == null || buttonDTOList.size() == 0){
            return;
        }
        for (ButtonDto b:buttonDTOList) {
            b.setId(null);
            b.setCreateTime(null);
            b.setLevel(null);
            b.setOrders(null);
            b.setParentId(null);
            b.setStatus(null);
            b.setUpdateTime(null);
            b.setUseStatus(null);
            if(b.getSub_button() != null){
                filterFiled(b.getSub_button());
            }
        }
    }


}
