package com.tian.happyfood.controller.common;

import com.tian.common.other.BusinessException;
import com.tian.common.validation.Validate;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于AOP切面实现统一的日志管理
 * Created by Administrator on 2016/11/16 0016.
 */
@Aspect
@Component
public class LogAspect {
    private final Logger logger = Logger.getLogger(LogAspect.class);

    //    @Before(value = "execution(* com..controller..*.*(..))")
    public void validate(JoinPoint jp) throws Exception {
        //获取参数的值
        Object[] args = jp.getArgs();
        // 获取当前拦截到方法对象
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Map<Annotation[], Object> map = new HashMap<Annotation[], Object>();
        //注解二阶数组
        Annotation[][] annotationss = targetMethod.getParameterAnnotations();
        for (int i = 0; i < annotationss.length; i++) {
            map.put(annotationss[i], args[i]);
        }
        for (Annotation[] a : map.keySet()) {
            //查看是否有注解,没有注解,不用验证
            if (a == null || a.length == 0) {
                continue;
            }
            // 有注解,按照规则进行一个个解析,看看是否通过
            for (int i = 0; i < a.length; i++) {
                boolean b = Validate.verify(a[i], map.get(a));
                if (!b) {
                    logger.error(map.get(a) + "  校验不通过");
                    throw new BusinessException(500, "参数: " + map.get(a) + " 校验不通过!");
                }
                logger.debug(map.get(a) + "  校验通过");
            }
        }

    }


    @Before(value = "execution(* com.tian.happyfood.controller..*Controller.*(..))")
    public void before(JoinPoint jp) throws Throwable {
        //调用方法前先进行登录验证
//        checkLogin();
        // 参数有效性验证
        validate(jp);

        logger.info("====> process in : " + jp.getSignature());
        StringBuffer params = new StringBuffer();
        String oneParam;
        for (Object o : jp.getArgs()) {
            if (o == null) {
                continue;
            }
            if (o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof String || o instanceof Boolean) {
                oneParam = o + "";
            } else {
                oneParam = o.toString();
            }
            params.append(oneParam + " ; ");
        }
        logger.info("====> param : " + params.toString());

    }

    private void checkLogin() {
        // 获取httpServletRequest对象
        HttpServletRequest request;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            logger.info("非页面请求,没有request对象,不拦截");
            return;
        }
        request = servletRequestAttributes.getRequest();
        String uri = request.getRequestURI();
        // ,如果本次方法就是登录方法则,直接放行
        if (uri.contains("login")) {
            return;
        }
        if (request.getSession(true).getAttribute("user") == null) {
            try {
                HttpServletResponse response = servletRequestAttributes.getResponse();
                // 重定向到登录页面
                // 项目配置的访问路径
                String s = request.getContextPath();
                // 当前请求的访问路径,包括了项目访问路径,但是不包括ip和端口号
                String s2 = request.getRequestURI();
                // 本次请求的完整路径,也就是浏览器地址栏中看到的.
                String s3 = request.getRequestURL().toString();

                // 处理上面几个路径,得到项目启动默认访问的路径,一般为登录页面.
                response.sendRedirect(s3.replace(s2,"")+s);
                throw new BusinessException(501, "请先登录");
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException(501, "请先登录");
            }
        }
    }


    /**
     * 环绕通过会拦截两次,所以换成了前置拦截+后置拦截,来实现日志.
     * 且因为如果同时用了切面和spring的拦截器,会导致,切面中会拦截到多次spring底层的方法,所以,
     * 现在登录验证想办法也通过拦截器来实现,至于出现问题的原因,和解决办法,还有待解决
     *
     * @return
     * @throws Throwable
     */
//    @Around(value = "execution(* com.tian.springmvcmybatis.controller..*.*(..))")
//    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
//        logger.info("====> process in : " + pjp.getSignature());
//        StringBuffer params = new StringBuffer();
//        String oneParam;
//        for (Object o : pjp.getArgs()) {
//            if(o== null){
//                continue;
//            }
//            if(o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof String || o instanceof Boolean ){
//                oneParam = o + "";
//            }else {
//                oneParam = o.toString();
//            }
//            params.append(oneParam + " ; ");
//        }
//        logger.info("====> param : " + params.toString());
//
//        Object result = pjp.proceed();
//        logger.info("====> result : "+result.toString());
//        return result;
//    }
    @AfterReturning(returning = "result", value = "execution(* com.tian.happyfood.controller..*.*(..))")
    public void after(Object result) throws Throwable {
        logger.info("====> result : " + (result == null ? "null" : result.toString()));
    }

}
