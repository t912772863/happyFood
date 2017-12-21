package com.tian.happyfood.service.wechatutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tian.common.util.HttpUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public abstract class WXUtils {
    /**
     * 微信APPID
     */
    protected static final String APPID_WX = "wxb9a14a469bc18d05";
    /**
     * 微信APPSECRT
     */
    protected static final String APPSECRT_WX = "d3feb528bf36ca852b25ddd984a72d0e";

    /**
     * 获取access_token的接口地址
     */
    protected static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * 获取微信的access_token
     * @return
     */
    protected static String getWXAccessToken(){
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
