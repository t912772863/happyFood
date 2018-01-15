package com.tian.happyfood.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 该过滤器, 主要用于过滤所有页面来的请求参数, 防止XSS攻击
 * Created by Administrator on 2018/1/15 0015.
 */
public class XSSFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request),response);
    }

    public void destroy() {

    }
}
