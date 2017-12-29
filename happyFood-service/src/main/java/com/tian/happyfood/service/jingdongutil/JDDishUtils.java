package com.tian.happyfood.service.jingdongutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tian.common.util.HttpUtils;
import com.tian.happyfood.service.jingdongutil.bean.JDDish;
import com.tian.happyfood.service.jingdongutil.bean.JDDishVariety;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 京东万象数据, 菜谱大全接口
 * Created by Administrator on 2017/12/28 0028.
 */
public class JDDishUtils {
    private static final Logger logger = LoggerFactory.getLogger(JDDishUtils.class);
    /**
     * 京东万象数据服务, 菜谱大全, 调用接口的appkey
     */
    private static final String JD_DISH_INTERFACE_APPKEY = "847cb4ea4448ce8dc85bc76284aa9a28";

    /**
     * 菜谱搜索接口地址
     */
    private static final String JD_SEARCH_DISH = "https://way.jd.com/jisuapi/search?";
    /**
     * 京东万象, 菜品分类接口
     */
    private static final String JD_RECIPE_CLASS = "https://way.jd.com/jisuapi/recipe_class?";

    /**
     * 根据分类,查询该人类下的菜品接口地址
     */
    private static final String JD_BY_CLASS = "https://way.jd.com/jisuapi/byclass?";
    /**
     * 根据菜品ID查询菜品详情
     */
    private static final String JD_DETAIL = "https://way.jd.com/jisuapi/detail?";
    /**
     * 根据关键词查询菜谱
     *
     * @param keyword 关键词
     * @param num     菜品数, 最多20
     * @return 返回根据关键词匹配到的对应的菜品
     */
    public static List<JDDish> searchDish(String keyword, int num) {
        String url = JD_SEARCH_DISH + "keyword=" + keyword + "&num=" + num + "&appkey=" + JD_DISH_INTERFACE_APPKEY;
        String result = requestJD(url);
        logger.info("关键词查询菜品返回: " + result);
        if(result == null){
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        List<JDDish> jDdishList = JSONArray.parseArray(jsonArray.toJSONString(), JDDish.class);
        return jDdishList;
    }

    /**
     * 查询所有的菜品分类
     * @return
     */
    public static List<JDDishVariety> recipeClass() {
        String url = JD_RECIPE_CLASS + "appkey=" + JD_DISH_INTERFACE_APPKEY;
        String result = requestJD(url);
        logger.info("查询菜品分类返回: " + result);
        if(result == null){
            return null;
        }
        List<JDDishVariety> jdDishVarietyList = JSONArray.parseArray(result, JDDishVariety.class);
        return jdDishVarietyList;
    }

    /**
     * 根据分类, 查询该分类下指定数量的菜品
     * @param classId
     * @param start
     * @param num
     * @return
     */
    public static List<JDDish> byClass(String classId, int start, int num){
        String url = JD_BY_CLASS + "classid=" + classId +"&start="+start+ "&num=" + num + "&appkey=" + JD_DISH_INTERFACE_APPKEY;
        String result = requestJD(url);
        logger.info("按分类检索菜品返回: " + result);
        if(result == null){
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        List<JDDish> jDdishList = JSONArray.parseArray(jsonArray.toJSONString(), JDDish.class);
        return jDdishList;
    }

    /**
     * 根据菜品id, 查询菜品的详情
     * @param id
     * @return
     */
    public static JDDish getDetail(String id){
        String url = JD_DETAIL + "id="+id + "&appkey="+JD_DISH_INTERFACE_APPKEY;
        String result = requestJD(url);
        if(result == null){
            return null;
        }
        JDDish jdDish = JSON.parseObject(result, JDDish.class);
        return jdDish;
    }

    private static String requestJD(String url) {
        String result = null;
        try {
            result = HttpUtils.doGet(url);
        } catch (IOException e) {
            logger.error("请求京东万象异常: ", e);
        }
        logger.info("请求京东万象返回: " + result);
        if (result != null && !"".equals(result)) {
            JSONObject jsonObject = JSON.parseObject(result);
            if ("10000".equals(jsonObject.getString("code"))) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                if ("0".equals(jsonObject1.getString("status"))) {
                    result = jsonObject1.getString("result");
                    return result;
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
//        searchDish("白菜", 2);
//        recipeClass();
//        byClass("2",0,2);
        getDetail("5");
    }

}
