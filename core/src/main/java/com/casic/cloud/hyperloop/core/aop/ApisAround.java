package com.casic.cloud.hyperloop.core.aop;

import com.alibaba.fastjson.JSONObject;
import com.casic.cloud.hyperloop.common.exception.base.BaseException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.model.result.ApiResult;
import com.casic.cloud.hyperloop.common.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Configuration
@Slf4j
public class ApisAround implements Ordered {

    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution(* com.casic.cloud.hyperloop.core..controller.*Controller.*(..))")
    public void recordLog() {
    }

    @Around("recordLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object o;
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        //参数数组
        Object[] params = pjp.getArgs();
        ApiOperation apiOperation = null;
        if (targetMethod.isAnnotationPresent(ApiOperation.class)) {
            apiOperation = targetMethod.getAnnotation(ApiOperation.class);
        }
        Class<?> z = targetMethod.getDeclaringClass();
        Api api = null;
        if (z.isAnnotationPresent(Api.class)) {
            api = z.getAnnotation(Api.class);
        }

        Long now = System.currentTimeMillis();
        log.info("==========================开始==========================");
        log.info("请求时间->" + DateUtil.LocalDateTime2String(LocalDateTime.now()));

        if (apiOperation != null) {
            log.info("API方法->" + (api == null ? "" : "【" + api.value() + "】->") + apiOperation.value());
        }
        log.info("代码路径->" + z.toString() + "." + targetMethod.getName());
        log.info("请求路径->" + request.getRequestURI());
        try {
            log.info("请求参数->" + JSONObject.toJSONString(params));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            o = pjp.proceed();
        } catch (BaseException e) {
            return e.getApiResult();
        } catch (RuntimeException e2) {
            e2.printStackTrace();
            return ApiResult.addFail(ApiErrorCode.server_exception);
        } catch (Exception e3) {
            e3.printStackTrace();
            return ApiResult.addFail(ApiErrorCode.server_exception);
        } finally {
            log.info("执行耗时->" + (System.currentTimeMillis() - now));
            log.info("==========================结束==========================");
        }

        return o;
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
