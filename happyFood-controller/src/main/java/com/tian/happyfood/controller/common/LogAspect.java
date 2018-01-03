package com.tian.happyfood.controller.common;

import com.tian.common.other.BusinessException;
import com.tian.common.validation.Validate;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

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


    @AfterReturning(returning = "result", value = "execution(* com.tian.happyfood.controller..*.*(..))")
    public void after(Object result) throws Throwable {
        logger.info("====> result : " + (result == null ? "null" : result.toString()));
    }

}
