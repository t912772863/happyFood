package com.tian.happyfood.service.common;

import java.util.HashSet;
import java.util.Set;

/**
 * 系统数据缓存
 * Created by Administrator on 2017/12/29 0029.
 */
public class SystemCache {
    /**
     * 缓存本地数据库中所遥菜品名
     * K 菜品名,  V 菜品ID
     */
    public static Set<String> dishNameSet = new HashSet<String>();
}
