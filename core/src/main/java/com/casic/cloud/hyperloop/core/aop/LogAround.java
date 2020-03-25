package com.casic.cloud.hyperloop.core.aop;


import com.casic.cloud.hyperloop.common.utils.DateUtil;
import com.casic.cloud.hyperloop.common.utils.IpUtil;
import com.casic.cloud.hyperloop.core.annotation.LogCustom;
import com.casic.cloud.hyperloop.core.constants.CoreConstants;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 自定义日志注解切面
 *
 * @author lvbaolin
 */

@Aspect
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
     * @param prod
     * @return
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint prod) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) prod.getSignature();
        Method method = methodSignature.getMethod();
        //拿到注解
        LogCustom annotation = method.getAnnotation(LogCustom.class);

        //用户id
        Long userId = (Long) request.getAttribute(CoreConstants.REQUEST_ATTR_USERID);
        //模块code
        Integer code = annotation.module().getCode();
        //操作
        Integer type = annotation.type().getCode();
        //ip
        String ip = IpUtil.getIpAddr(request);
        //Date
        Date now = DateUtil.convert2Date(LocalDateTime.now());

        //TODO
        //入库

        log.info("userId={},code={},type={},ip={},now={}", userId, code, type, ip, now);
        return prod.proceed();
    }

    @Override
    public int getOrder() {
        return 5;
    }

}