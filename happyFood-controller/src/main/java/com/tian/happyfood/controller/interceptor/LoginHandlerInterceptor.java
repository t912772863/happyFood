package com.tian.happyfood.controller.interceptor;

import com.tian.happyfood.dao.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器, 已经弃用, 改用切面实现,对所有调用controller的方法都拦截,进行是否登录的检验
 * Created by Administrator on 2016/11/30 0030.
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        User user = (User)request.getSession(true).getAttribute("user");
        if(user == null){
            String s = request.getContextPath();
            // 当前请求的访问路径,包括了项目访问路径,但是不包括ip和端口号
            String s2 = request.getRequestURI();
            // 本次请求的完整路径,也就是浏览器地址栏中看到的.
            String s3 = request.getRequestURL().toString();
            // 重定向
            response.sendRedirect(s3.replace(s2,"")+s+"/index.html");
            // 转发
//            request.getRequestDispatcher("/index.html").forward(request,response);
        }
        return user!=null;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
