package com.casic.cloud.hyperloop.core.controller;

import com.casic.cloud.hyperloop.core.constants.UrlMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: LDC
 * @Date: 2020/01/03 11:03
 * @version: V1.0
 */
@Slf4j
@RestController
@Api(tags = {"LoginController"}, description = "登录相关接口")
public class LoginController {

    @Autowired
    private HttpServletRequest request;

    @Value("${safelogin.login.url}")
    private String loginUrl;

    /***
     * @Description:重定向接口
     * @Author: LDC
     * @Date: 2020/2/19 16:14
     */
    @ApiOperation(value = "重定向接口-GET请求", httpMethod = "GET")
    @ApiParam(name = "callBack", value = "重定向地址", required = true)
    @GetMapping(UrlMapping.REDIRECT)
    public ModelAndView redirect(@RequestParam(value = "callBack") String callBack) {

        if (StringUtils.isNotEmpty(callBack)) {
            if (!callBack.startsWith("http") && !callBack.startsWith("https")) {
                callBack = "http://" + callBack;
            }

            log.info("【重定向接口】成功，跳转地址url={}",callBack);
            return new ModelAndView(new RedirectView(callBack));
        }
        return null;
    }

    /***
     * @Description:登出接口
     * @Author: LDC
     * @Date: 2020/2/19 16:15
     */
    @ApiOperation(value = "登出接口-GET请求", httpMethod = "GET")
    @GetMapping(value = UrlMapping.LOGOUT)
    public ModelAndView logout(HttpServletRequest request) {
        //获取域名后拼接的参数。
        //service=登录地址 + 自定义参数
        String str = request.getQueryString();
        String url = this.loginUrl + "/logout" + "?" + str;
        //清除session
        request.getSession().invalidate();

        log.info("【登出接口】成功，跳转地址url={}",url);
        return new ModelAndView(new RedirectView(url));
    }
    
}
