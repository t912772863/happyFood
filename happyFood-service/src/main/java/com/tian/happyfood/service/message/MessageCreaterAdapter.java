package com.tian.happyfood.service.message;

import com.tian.happyfood.service.common.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息构建者适配器.
 * 这里主要是因为菜谱功能,做了两个版本, 第一个版本所依赖的京东万像数据现在无法免费调用了,所以又换了另一个方式的.
 * 可以通过配置文件中配置值, 来指定当前系统用哪一个版本的功能
 * Created by tianxiong on 2019/1/24.
 */
@Component
public class MessageCreaterAdapter {
    @Autowired
    private DefaultMessageCreator defaultMessageCreater;
    @Autowired
    private JDMessageCreator jdMessageCreator;
    @Autowired
    private WebMessageCreator webMessageCreator;

    /**
     * 根据消息类型和消息内容返回要用的消息构建者
     * @param dishVersion
     * @return
     */
    public IMessageCreator getCreator(String dishVersion){
        if (SystemConstants.DISH_VERSION_JD.equals(dishVersion)) {
            return jdMessageCreator;
        } else if (SystemConstants.DISH_VERSION_WEB.equals(dishVersion)) {
            return webMessageCreator;
        }else {
            return defaultMessageCreater;
        }
    }


}
