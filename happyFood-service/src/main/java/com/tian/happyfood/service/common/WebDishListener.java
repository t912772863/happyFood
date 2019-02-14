package com.tian.happyfood.service.common;

import com.tian.common.util.HttpUtils;
import com.tian.common.util.PinYinUtil;
import com.tian.happyfood.dao.entity.Dish;
import com.tian.happyfood.service.IDishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * web版本的菜品拉取
 * Created by tianxiong on 2019/1/25.
 */
public class WebDishListener implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(WebDishListener.class);
    /** 网上的菜谱做法, 只需要把特定的字符串替换就是自己要的菜品了*/
    private static final String DEFAULT_URL = "https://m.meishij.net/html5/zuofa/qingzhengluyu.html";
    private static IDishService dishService;



    public void onMessage(Message message) {
        if(dishService == null){
            dishService = ApplicationHelper.getApplicationContext().getBean(IDishService.class);
        }
        FileOutputStream outputStream = null;
        try {
            String s = ((TextMessage)message).getText();
            log.info("WebDishListener, onMessage: "+ s);
            // 把汉字转成拼音,
            String pinyinStr = PinYinUtil.getSpell(s,false);
            // 拼接地址,
            String targetUrl = DEFAULT_URL.replace("qingzhengluyu", pinyinStr);
            // 如果正常则保存返回的html为一个静态页面, 请求的时候设置一下UA, 返回的是支持手机格式的
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");
            String result = HttpUtils.doGet(targetUrl, headers);
            // 保存图片, html等文件
            String imgUrl = getImgUrl(s, result);
            if(imgUrl == null){
                log.info("not found this dish: "+s);
                message.acknowledge();
                return;
            }
            // 简单描述
            String descreption = getDescription(result);
            // 保存静态页面
            long now = System.currentTimeMillis();
            String fileFullName = Config.config.getString("file_local_pre")+"/html/"+now+".html";
            File htmlFile = new File(fileFullName);
            outputStream = new FileOutputStream(htmlFile);
            outputStream.write(result.getBytes("utf-8"));

            // 保存数据库
            Dish dish = new Dish();
            dish.setName(s);
            dish.setPic(imgUrl);
            dish.setContent(descreption);
            dish.setType(2);
            dish.setDetailUrl("/html/"+now+".html");
            dishService.insert(dish);
            // 消息确认
            message.acknowledge();
        } catch (JMSException e) {
            log.error("收到消息异常",e);
        } catch (IOException e) {
            log.error("consume msg error",e);
        }finally {
            try {
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 从成功的html文本中找到背景大图, 如果找不到,则返回null, 说明没有该菜品
     * 类似于下面这个文件就是背景大图
     * <div class="img" style="background:url(http://s1.ig.meishij.net/p/20130613/47b8be11272cb69d36f92ea984c636ad.jpg) center /cover no-repeat;background-size:cover;"></div>
     * @param s 输入的菜名
     * @param result 调用接口返回的html页面
     * @return
     */
    private static String getImgUrl(String s, String result) {
        // 如果整个页面中都没有包含传入的菜名, 说明肯定是没找到菜品, 返回的其它页面, 所以忽略
        if(!result.contains(s)){
            return null;
        }

        int index = result.indexOf("style=\"background:url");
        if(index == -1){
            return null;
        }
        int startIdx = result.indexOf("(", index);
        if(startIdx == -1){
            return null;
        }
        int endIdx = result.indexOf(")", startIdx);
        if(endIdx == -1){
            return null;
        }
        String imgUrl = result.substring(startIdx+1, endIdx);
        return imgUrl;
    }

    public static void main(String[] args) {
        String s = "qingzhengluyu";
        String pinyinStr = PinYinUtil.getSpell(s,false);
        // 拼接地址,
        String targetUrl = DEFAULT_URL.replace("qingzhengluyu", pinyinStr);
        // 如果正常则保存返回的html为一个静态页面
        try {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");
            String result = HttpUtils.doGet(targetUrl);
            File file = new File("D:/tomcat/tomcatFile/html/temp.html");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(result.getBytes("utf-8"));

            System.out.println(getImgUrl(s,result));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取页面中的描述内容
     * 类型于下这个样子
     * <meta name="description" content="家常豆腐的做法，网友真实发布的家常豆腐的家常做法，以及家常豆腐所需食材、清晰详尽的家常豆腐步骤图，同时欢迎您分享自己家常豆腐的做法和诀窍，欢迎访问美食杰！" />
     * @param result
     * @return
     */
    public static String getDescription(String result) {
        int startIdx = result.indexOf("<meta name=\"description\" content=\"");
        if(startIdx == -1){
            return "";
        }
        int endIdx = result.indexOf("\" />", startIdx);
        if(endIdx == -1){
            return "";
        }
        return result.substring(startIdx+"<meta name=\"description\" content=\"".length(), endIdx);
    }
}
