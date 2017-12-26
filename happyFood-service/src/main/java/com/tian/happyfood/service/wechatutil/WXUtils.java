package com.tian.happyfood.service.wechatutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tian.common.util.HttpUtils;
import com.tian.common.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public abstract class WXUtils {
    private static final Logger logger = LoggerFactory.getLogger(WXUtils.class);
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
        String wxAccessToken = JedisUtil.get("wxAccessToken");
        if(wxAccessToken != null && !"".equals(wxAccessToken)){
            return wxAccessToken;
        }
        try {
            String result = HttpUtils.doGet(GET_ACCESS_TOKEN_URL+"&appid="+APPID_WX+"&secret="+APPSECRT_WX);
            logger.info("获取微信access_token返回: result = "+result);
            JSONObject jsonObject = JSON.parseObject(result);
            Integer errcode = jsonObject.getInteger("errcode");
            if(errcode != null && errcode != 0){
                logger.error("获取微信access_token异常");
                return null;
            }

            String accessToken = jsonObject.getString("access_token");
            // 把最新获取到的accss_token加入到缓存中, 官网有效时间为90s, 这里设为80s, 防止临界值问题
            JedisUtil.set("wxAccessToken", accessToken, 80);
            return accessToken;
        } catch (IOException e) {
            logger.error("获取微信access_token异常: ",e);
        }
        return null;
    }
}
