package com.tian.happyfood.service.wechatutil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tian.common.util.HttpUtils;
import com.tian.happyfood.service.wechatutil.bean.wxuser.WXUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tian.common.util.HttpUtils.doGet;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public class WXUserUtils extends WXUtils {
    private static final Logger logger = LoggerFactory.getLogger(WXUserUtils.class);
    /**
     * 单个获取微信用户信息接口地址
     */
    private static final String GET_USER_BY_OPENID = "https://api.weixin.qq.com/cgi-bin/user/info?lang=zh_CN&";
    /**
     * 多个获取用户信息接口
     */
    private static final String GET_USER_BY_OPENIDS = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?";
    /**
     * 获取用户的opeids
     */
    private static final String GET_USER_OPENIDS = "https://api.weixin.qq.com/cgi-bin/user/get?";

    /**
     * 根据openid单个获取微信用户信息
     *
     * @param openId
     * @return
     */
    public static WXUser getUserBuOpenId(String openId) {
        String url = GET_USER_BY_OPENID + "access_token=" + getWXAccessToken() + "&openid=" + openId;
        logger.info("请求单个用户信息: url = "+url);
        String result = null;
        try {
            result = doGet(url);
            logger.info("请求单个用户信息接口返回: result = "+result);
            Integer errcode = JSONObject.parseObject(result).getInteger("errcode");
            if(errcode != null && errcode != 0){
                logger.error("请求单个用户信息异常");
                return null;
            }

        } catch (IOException e) {
            logger.error("单个获取微信用户信息异常", e);
        }
        if (result != null) {
            WXUser wxUser = JSONObject.parseObject(result, WXUser.class);
            return wxUser;
        }
        return null;
    }

    /**
     * 根据多个openid获取用户信息
     *
     * @param openIds 用户openid集合
     * @return
     */
    public static List<WXUser> getUserByOpenIds(List<String> openIds) {
        // 请求参数
        String requestData = transformParams(openIds);
        // 请求url
        String url = GET_USER_BY_OPENIDS + "access_token="+getWXAccessToken();

        try {
            String result = HttpUtils.doPostOfJson(url, requestData);
            Integer errcode = JSONObject.parseObject(result).getInteger("errcode");
            if(errcode != null && errcode != 0){
                logger.error("请求多个用户信息异常");
                return null;
            }
            String arryStr = JSONObject.parseObject(result).getString("user_info_list");
            List<WXUser> wxUsers = JSONArray.parseArray(arryStr, WXUser.class);
            return wxUsers;
        } catch (Exception e) {
            logger.error("批量查询微信用户信息异常", e);
        }
        return null;
    }

    /**
     * 查询公众号下所有的用户openid, 当量比较多时, 方法会递归调用查询出所有.
     * @param nextOpenId 下次查询开始的openid, 首次调用传空串, 后面会递归
     * @param openIdList openid集合, 传个空 , 运行完把所有的加进来.
     */
    public static void getUserOpenIds(String nextOpenId, List<String> openIdList){
        String url = GET_USER_OPENIDS + "access_token="+getWXAccessToken()+"&next_openid="+nextOpenId;
        try {
            String result = HttpUtils.doGet(url);
            logger.info("获取用户openids返回: result = "+result);
            Integer errcode = JSONObject.parseObject(result).getInteger("errcode");
            if(errcode != null && errcode != 0){
                logger.error("获取用户openids失败");
                return;
            }
            String data = JSONObject.parseObject(result).getString("data");
            String openidArrStr = JSONObject.parseObject(data).getString("openid");
            List<String> openids = JSONArray.parseArray(openidArrStr, String.class);
            openIdList.addAll(openids);

            String next = JSONObject.parseObject(result).getString("next_openid");
            // 如果接口返回有下一个openid,说明还有数据, 则进行递归调用, 把所有数据查询出来
            if(next != null && !"".equals(next)){
                getUserOpenIds(next, openIdList);
            }
        } catch (IOException e) {
            logger.error("获取用户openIds异常", e);
        }
    }

    private static String transformParams(List<String> openIds) {
        // 接口请求的参数
        JSONObject jsonObject = new JSONObject();
        // id集合
        List<JSONObject> subJsonList = new ArrayList<JSONObject>();
        for (String s : openIds) {
            JSONObject subJson = new JSONObject();
            subJson.put("openid", s);
            subJson.put("lang", "zh_CN");
            subJsonList.add(subJson);
        }
        jsonObject.put("user_list", subJsonList);
        String requestData = jsonObject.toJSONString();
        logger.info("根据openid查询用户信息接口, 请求参数: " + requestData);
        return requestData;
    }



    public static void main(String[] args) {
        List<String> idlist = new ArrayList<String>();
        getUserOpenIds("", idlist);
        System.out.println(idlist);

    }


}
