package com.tian.happyfood.service.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by tianxiong on 2019/1/25.
 */
@Component
public class ApplicationHelper implements ApplicationContextAware {
    public ApplicationHelper(){
        System.out.println("init spring application");
    }

    private static ApplicationContext applicationContext;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
