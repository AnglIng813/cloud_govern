package com.casic.cloud.hyperloop.core.aop;

import com.alibaba.fastjson.JSONObject;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.model.result.ApiResult;
import com.casic.cloud.hyperloop.common.utils.AesEncryptUtil;
import com.casic.cloud.hyperloop.common.utils.ConvertPathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Aspect
@Configuration
@Slf4j
public class WebLoginAround implements Ordered {

    @Autowired
    private HttpServletRequest request;

    @Value("${safelogin.url.patterns}")
    private String patterns;

    @Value("${safelogin.login.url}")
    public String loginUrl;


    @Pointcut("execution(* com.casic.cloud.hyperloop.core..controller.*Controller.*(..))")
    public void recordLog() {
    }

    @Around("recordLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        //参数数组
        Object[] params = pjp.getArgs();

        String servletPath = request.getServletPath();
        if (this.isPatterns(servletPath) && this.authorization()) {
            //return ApiResult.addFail(ApiErrorCode.no_login_web);//认证失败 401
            /**
             * 此处两种方式，一种前端ajax 返回401 由前端控制跳转
             * 第二种，则为后台控制
             * TODO 此处loginUrl以注解方式取值待验证
             */
            String callback = request.getRequestURL() + "?" + request.getQueryString();
            if(StringUtils.isNotEmpty(callback)){
                loginUrl += "?callback=" + callback;
            }
            this.getResponse().sendRedirect(loginUrl);
        }

        return pjp.proceed();
    }

    private boolean authorization() {
        try {
            String confusionStr = "Hyperloop ";
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isEmpty(authorization) || !authorization.startsWith(confusionStr)) {
                return true;
            }
            String token = authorization.substring(confusionStr.length());
            if (StringUtils.isEmpty(token) || StringUtils.equalsIgnoreCase("undefined", token)) {
                return true;
            }
            String decode = URLDecoder.decode(token, System.getProperty("file.encoding"));
            //解密
            JSONObject obj = JSONObject.parseObject(AesEncryptUtil.aesCbcPkcs5PaddingDecrypt(decode));
            if (Objects.isNull(obj)) {
                return true;
            }
            if (!StringUtils.equals(obj.getString("typ"), "JWT")
                    || !StringUtils.equals(obj.getString("project"), "tianyicloud")) {
                return true;
            }

        } catch (Exception e) {
            log.info("认证失败");
            e.printStackTrace();
            return true;
        }
        return false;
    }

    private boolean isPatterns(String servletPath) {
        //TODO 待验证
        List<String> splits = Arrays.asList(patterns.split(","));
        if (!(splits.size() > 0)) {
            return false;
        }
        return ConvertPathUtil.isWhiteUrlList(servletPath, splits);
    }

    //获取response
    private HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
