package com.tian.happyfood.controller.common;

import com.tian.common.util.HttpUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

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
        // 获取请求地址的第一层级
//        String requestUrl = httpServletRequest.getRequestURI();
//        requestUrl = requestUrl.substring(0,requestUrl.indexOf("/",1));
        // 免拦截,直接放行
//        if(uncheckUrls.contains(requestUrl)){
//            return true;
//        }
        // 先查看用户是否为登录状态
        HttpSession session = httpServletRequest.getSession(true);
        if (session.getAttribute("user")!=null){
            return true;
        }else{
            String token = httpServletRequest.getParameter("token");
            if(token != null){
                // 验证token
                String result = HttpUtils.doGet("http://localhost:8086/singlesign/login/check_token?token="+token);
                JSONObject jsonObject = new JSONObject(result);
                boolean index = jsonObject.getBoolean("data");
                if(index){
                    // 验证通过, 创建session
                    session.setAttribute("user", token);
                    return true;
                }
            }

            // 没有登录, 重定向到登录页面  http://localhost:8082/singlesign/   http://localhost:8086/singlesign/
            String url = httpServletRequest.getRequestURL().toString()+"?";
            Map<String ,String[]> map = httpServletRequest.getParameterMap();
            for (Map.Entry<String , String[]> entry:map.entrySet()) {
                url += entry.getKey()+"="+(entry.getValue())[0]+"&";
            }
            url = url.substring(0, url.length()-1);


            // 带着本次请求的url, 跳转到认证中心去
            httpServletResponse.sendRedirect("http://localhost:8086/singlesign/login/to_index?url="+url);
            return false;
        }
        // 不能放行.说明未登录
//        throw new BusinessException(501,"请先登录");
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
