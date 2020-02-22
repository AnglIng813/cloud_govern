package com.casic.cloud.hyperloop.login.filter;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:跨域配置
 * @Author: LDC
 * @Date: 2018/12/11 10:04
 * @version: V1.0
 */
@Component
public class ApiOriginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //处理跨域问题
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //判断是否存在请求源
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String requestOrigin = request.getHeader("Origin");
        if (StringUtils.isEmpty(requestOrigin)) {
            requestOrigin = "*";
        }
        // 允许指定域访问跨域资源
        response.setHeader("Access-Control-Allow-Origin", requestOrigin);
        // 允许客户端携带跨域cookie，此时origin值不能为“*”，只能为指定单一域名
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,PATCH,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Date, X-Api-Version, X-File-Name,Token,Cookie,SecretKey,UniqueKey,AppKey,language,sign_type,public_key,sign");
        response.setHeader("Access-Control-Expose-Headers", "X-Requested-With");

        //这里通过判断请求的方法，判断此次是否是预检请求，如果是，立即返回一个204状态吗，标示，允许跨域；
        // 预检后，正式请求，则不走此判断
        if ("OPTIONS".equals(request.getMethod())){

            response.setHeader("Content-Type","application/json;charset=UTF-8");
            response.addHeader("Access-Control-Max-Age", "1");
            response.setStatus(200);
            return;
        }else{
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
