package com.casic.cloud.hyperloop.core.aop;

import com.casic.cloud.hyperloop.common.utils.IpUtil;
import com.casic.cloud.hyperloop.core.annotation.LogCustom;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect // 1.表明这是一个切面类
@Component
@Slf4j
public class LogAround implements Ordered {

    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.casic.cloud.hyperloop.core.annotation.LogCustom)")
    public void logPointCut() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @return
     */
    @Around("logPointCut()")
    public Object around(JoinPoint joinPoint) {
        System.out.println("--------------------开始--------------------");
        long start = System.currentTimeMillis();
        //获取请求ip
        String ip = IpUtil.getIpAddr(request);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取方法
        Method method = methodSignature.getMethod();
        //模块名称
        String module = method.getAnnotation(LogCustom.class).module();
        //操作
        String operation = method.getAnnotation(LogCustom.class).operation();
        String operationType = method.getAnnotation(LogCustom.class).operationType().getDescription();
        //用户名
        String userName = "userName";
        //请求参数
        Object[] param = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object o : param) {
            sb.append(o + "; ");
        }
        Object ob = null;
        try {
            ob = ((ProceedingJoinPoint) joinPoint).proceed();
            if (log.isInfoEnabled()) {
                log.info(module + "->" + userName + "进行" + operation + "，操作类型：" + operationType);
            }

        } catch (Throwable e) {
            if (log.isInfoEnabled()) {
                log.info("around " + joinPoint + "\tUse time : " + (System.currentTimeMillis() - start) + " ms with exception : " + e.getMessage());
            }
        } finally {
            if (log.isInfoEnabled()) {
                log.info("around " + joinPoint + "\tUse time : " + (System.currentTimeMillis() - start));
            }
            System.out.println("--------------------结束--------------------");
        }
        return ob;
    }

    @Override
    public int getOrder() {
        return 5;
    }



}