package com.tian.happyfood.service.common;

import java.util.ResourceBundle;

/**
 * 配置信息
 * Created by Administrator on 2017/11/3 0003.
 */
public class Config {
    public static ResourceBundle config = null;

    public static void init() {
        config = ResourceBundle.getBundle("business");
    }


}
