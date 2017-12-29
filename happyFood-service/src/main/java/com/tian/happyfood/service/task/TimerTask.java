package com.tian.happyfood.service.task;

import com.tian.happyfood.service.IDishService;
import com.tian.happyfood.service.common.SystemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定时任务类
 * Created by Administrator on 2016/12/9 0009.
 */
@Component
public class TimerTask {
    @Autowired
    private IDishService dishService;

    @PostConstruct
    private void init(){
        testTimerTask();
    }

    /**
     * 每半个小时, 把系统中所有的菜品名更新到缓存中
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void testTimerTask(){
        Set<String> set = dishService.queryDishName();
        SystemCache.dishNameSet = set;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("a");
        System.out.println(list.toString());

        Set<String> set = new HashSet<String>();
        set.add("a");
        set.add("b");
        set.add("a");
        System.out.println(set.toString());
    }
}
