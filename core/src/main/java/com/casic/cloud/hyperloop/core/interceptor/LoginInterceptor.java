package com.casic.cloud.hyperloop.core.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.model.result.ApiResult;
import com.casic.cloud.hyperloop.common.utils.AesEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * @Description: 由于aop影响效率，故此处使用拦截器
 * @Author: AnglIng
 * @Date: 2020/2/25 14:58
 * @version: V1.0
 */

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${safelogin.login.url}")
    public String loginUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*//TODO 此处可定义自定义注解，来跳过登录认证，更加灵活
        boolean flag = false;
        if(handler instanceof HandlerMethod){
            HandlerMethod h = (HandlerMethod)handler;
            if(h.getMethod().isAnnotationPresent("自定义注解.class")){
                MyAnnotation m = h.getMethodAnnotation("自定义注解.class");
                flag = m.required();
            }
        }*/


        /**
         * 此处未登录两种处理方式
         * 1.可进行页面跳转
         * 2.提示前端状态码(当前采用第二种方式)
         *
         */
        if (this.authorization(request)) {
            /*第一种
            String callback = request.getRequestURL() + "?" + request.getQueryString();
            if (StringUtils.isNotEmpty(callback)) {
                loginUrl += "?callback=" + callback;
            }
            response.sendRedirect(loginUrl);*/

            /*第二种*/
            StringBuffer callback = request.getRequestURL();
            if (StringUtils.isNotBlank(request.getQueryString())) {
                callback.append("?" + request.getQueryString());
            }

            //认证失败 401
            String msg = JSONObject.toJSONString(ApiResult.addFail(ApiErrorCode.no_login_web.getCode(), callback.toString(), null));
            response.getWriter().write(msg);
            return false;
        }

        return true;
    }

    private boolean authorization(HttpServletRequest request) {
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

}
