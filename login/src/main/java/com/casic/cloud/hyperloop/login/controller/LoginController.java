package com.casic.cloud.hyperloop.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.model.result.ApiResult;
import com.casic.cloud.hyperloop.common.utils.AesEncryptUtil;
import com.casic.cloud.hyperloop.common.utils.ConvertBeanUtils;
import com.casic.cloud.hyperloop.common.utils.Verify.VerifyCodeUtil;
import com.casic.cloud.hyperloop.login.constants.UrlMapping;
import com.casic.cloud.hyperloop.login.model.dto.LoginDTO;
import com.casic.cloud.hyperloop.login.model.vo.LoginVO;
import com.casic.cloud.hyperloop.login.service.LoginService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * @Description:
 * @Author: LDC
 * @Date: 2020/01/03 11:03
 * @version: V1.0
 */
@Slf4j
@RestController
@Api(tags = {"LoginController"}, value = "登录相关接口")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private HttpSession session;

    /***
     * @Description:登录接口
     * @Author: LDC
     * @Date: 2020/2/19 16:12
     */
    @ApiOperation(value = "登录接口-POST请求", httpMethod = "POST")
    @PostMapping(UrlMapping.SININ)
    public ApiResult login(@RequestBody LoginVO vo) {
        //校验
        //生成token返回前端，后期可以考虑使用redis替换此方式
        Long userId = loginService.validate(ConvertBeanUtils.converBean2Bean(vo, LoginDTO.class));
        log.info("【登录接口】校验成功,userName={}", vo.getUserName());
        return ApiResult.addSuccess(userId);
    }

    /***
     * @Description:重定向接口
     * @Author: LDC
     * @Date: 2020/2/19 16:14
     */
    @ApiOperation(value = "重定向接口-GET请求", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "callBack", value = "重定向地址", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true)
    })
    @GetMapping(UrlMapping.REDIRECT)
    public ModelAndView redirect(@RequestParam(value = "callBack") String callBack, @RequestParam(value = "userId") Long userId) {

        if (StringUtils.isEmpty(callBack) || Objects.isNull(userId)) {
            throw new CloudApiServerException("callBack|userId", ApiErrorCode.parameter_missing);
        }
        if (!callBack.startsWith("http") && !callBack.startsWith("https")) {
            callBack = "http://" + callBack;
        }
        callBack = this.convertToken(callBack, userId);

        log.info("【重定向接口】成功，跳转地址url={}", callBack);
        return new ModelAndView(new RedirectView(callBack));
    }


    /***
     * @Description:登出接口
     * @Author: LDC
     * @Date: 2020/2/19 16:15
     */
    @ApiOperation(value = "登出接口-GET请求", httpMethod = "GET")
    @ApiParam(name = "service", value = "登录地址", required = true)
    @GetMapping(value = UrlMapping.LOGOUT)
    public ModelAndView logout(HttpServletRequest request) {
        //获取域名后拼接的参数。
        //service=登录地址 + 自定义参数
        String str = request.getQueryString();
        //清除session
        request.getSession().invalidate();

        log.info("【登出接口】成功，跳转地址url={}",str);
        return new ModelAndView(new RedirectView(str));
    }

    /**
    * @Description: 生成验证码
    * @Author: AnglIng
    * @Date: 2020/2/26 15:02
    */
    @ResponseBody
    @ApiOperation(value = "生成验证码-GET请求", httpMethod = "GET")
    @GetMapping(value = UrlMapping.VERIFY_CODE)
    public void getVerifyNum(HttpSession session, HttpServletResponse response) {
        //配置response
        response.setContentType("image/jpeg");//设置输出类型为图片
        response.setHeader("Pragma", "no-Cache");//设置不缓存
        response.setHeader("Cache-Control", "no-Cache");//作用相当于上一行代码,通常与下一行代码合用
        response.setDateHeader("Expire", 0);
        //生成验证码图片，通过response返回前台
        new VerifyCodeUtil().generateImageCode(session, response);//不设置参数则使用默认配置
    }


    @NotNull
    private String convertToken(String callBack, Long userId) {
        try {
            JSONObject object = new JSONObject();
            //模拟jwt
            object.put("typ", "JWT");
            object.put("project", "tianyicloud");
            object.put("userId", userId);
            String token = AesEncryptUtil.aesCbcPkcs5PaddingEncrypt(object.toJSONString());
            if(StringUtils.contains(callBack, "?")){
                callBack += "&token=" + URLEncoder.encode(token, System.getProperty("file.encoding"));
            }else{
                callBack += "?token=" + URLEncoder.encode(token, System.getProperty("file.encoding"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("token转码失败");
            throw new CloudApiServerException(ApiErrorCode.server_exception);
        }
        return callBack;
    }
}
