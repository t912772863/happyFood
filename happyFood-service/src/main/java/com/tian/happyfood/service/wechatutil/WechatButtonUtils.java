package com.tian.happyfood.service.wechatutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tian.common.other.BusinessException;
import com.tian.common.util.HttpUtils;
import com.tian.happyfood.service.dto.ButtonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15 0015.
 */
public class WechatButtonUtils {
    /**
     * 日志工具
     */
    private static Logger logger = LoggerFactory.getLogger(WechatButtonUtils.class);

    /**
     * 微信APPID
     */
    private static final String APPID_WX = "wxb9a14a469bc18d05";
    /**
     * 微信APPSECRT
     */
    private static final String APPSECRT_WX = "d3feb528bf36ca852b25ddd984a72d0e";

    /**
     * 获取access_token的接口地址
     */
    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    /**
     * 上传按钮菜单的接口地址
     */
    private static final String UPLOAD_BUTTONS_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
    private static final String QUERY_BUTTONS_RUL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";

    public static boolean uploadButtons(List<ButtonDTO> buttonDTOList) throws Exception {
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
        int errcode = jsonObject.getIntValue("errcode");
        return errcode == 0;
    }

    public static boolean deleteButton(){
        return false;
    }

    /**
     * 过滤字段
     * @param buttonDTOList
     */
    private static void filterFiled (List<ButtonDTO> buttonDTOList){
        if(buttonDTOList == null || buttonDTOList.size() == 0){
            return;
        }
        for (ButtonDTO b:buttonDTOList) {
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

    /**
     * 获取微信的access_token
     * @return
     */
    private static String getWXAccessToken(){
        try {
            String result = HttpUtils.doGet(GET_ACCESS_TOKEN_URL+"&appid="+APPID_WX+"&secret="+APPSECRT_WX);
            JSONObject jsonObject = JSON.parseObject(result);
            String accessToken = jsonObject.getString("access_token");
            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
