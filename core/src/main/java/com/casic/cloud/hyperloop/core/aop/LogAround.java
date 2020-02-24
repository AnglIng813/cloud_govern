package com.casic.cloud.hyperloop.core.aop;

import com.casic.cloud.hyperloop.common.utils.IpUtil;
import com.casic.cloud.hyperloop.core.anno.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect // 1.表明这是一个切面类
@Component
@Slf4j
public class LogAround implements Ordered {


    @Pointcut("@annotation(com.casic.cloud.hyperloop.core.anno.Log)")
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
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取请求ip
        String ip = IpUtil.getIpAddr(request);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取方法
        Method method = methodSignature.getMethod();
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        //模块名称
        String module = method.getAnnotation(Log.class).module();
        //操作
        String operation = method.getAnnotation(Log.class).operation();
        String operationType = method.getAnnotation(Log.class).operationType();
        //        HttpSession session = request.getSession();
        //用户名
        String userName = "userName";
        //请求参数
        Object[] param = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object o : param) {
            sb.append(o + "; ");
        }

        long start = System.currentTimeMillis();
        Object ob = null;
        try {
            ob = ((ProceedingJoinPoint) joinPoint).proceed();
            if (log.isInfoEnabled()) {
                log.info("代码路径->" + methodDeclaringClass.toString() + "." + method.getName());
                log.info(module + "->" + userName + "进行" + operation+"，操作类型："+operationType);
                if (sb.length() > 0) {
                    log.info("请求参数->" + sb.toString());
                }else{
                    log.info("请求参数->无参数");
                }
                log.info("请求ip->" + ip);
                log.info("请求路径->" + request.getRequestURI());
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

//  log.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), sb);


}