package com.tian.happyfood.controller.common;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器, 已经弃用, 改用切面实现,对所有调用controller的方法都拦截,进行是否登录的检验
 * Created by Administrator on 2016/11/30 0030.
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
    /**
     * 配置一些请求地址不被拦截,比如登录请求等
     */
//    private List<String> uncheckUrls;

//    public List<String> getUncheckUrls() {
//        return uncheckUrls;
//    }

//    public void setUncheckUrls(List<String> uncheckUrls) {
//        this.uncheckUrls = uncheckUrls;
//    }

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
