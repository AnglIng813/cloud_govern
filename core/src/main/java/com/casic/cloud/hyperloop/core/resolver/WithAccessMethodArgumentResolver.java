package com.casic.cloud.hyperloop.core.resolver;

import com.alibaba.fastjson.JSONObject;
import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.utils.AesEncryptUtil;
import com.casic.cloud.hyperloop.common.utils.ConvertBeanUtils;
import com.casic.cloud.hyperloop.core.annotation.WithAccess;
import com.casic.cloud.hyperloop.core.constants.CoreConstants;
import com.casic.cloud.hyperloop.core.model.domain.User;
import com.casic.cloud.hyperloop.core.model.dto.UserDTO;
import com.casic.cloud.hyperloop.core.model.result.UserRes;
import com.casic.cloud.hyperloop.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * @Description:
 * @Author: AnglIng
 * @Date: 2020/2/25 14:15
 * @version: V1.0
 */
@Slf4j
public class WithAccessMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(WithAccess.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //没打注解，不需要处理
        WithAccess withAccess = methodParameter.getMethodAnnotation(WithAccess.class);
        if (Objects.isNull(withAccess)) {
            return null;
        }

        //打了注解且开启了认证
        if(withAccess.required()){
            //认证失败抛401
            Long userId = this.authorization(nativeWebRequest);
            if (Objects.isNull(userId)) {
                throw new CloudApiServerException(withAccess.apiErrorCode());
            }

            //返回用户
            if (methodParameter.getParameterType().equals(User.class)) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(userId);
                UserRes res = userService.selectByCondition(userDTO);
                return ConvertBeanUtils.converBean2Bean(res, User.class);
            }

        }

        return null;
    }


    private Long authorization(NativeWebRequest nativeWebRequest) {
        try {
            HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            String confusionStr = "Hyperloop ";
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isEmpty(authorization) || !authorization.startsWith(confusionStr)) {
                return null;
            }
            String token = authorization.substring(confusionStr.length());
            if (StringUtils.isEmpty(token) || StringUtils.equalsIgnoreCase("undefined", token)) {
                return null;
            }
            String decode = URLDecoder.decode(token, System.getProperty("file.encoding"));
            //解密
            JSONObject obj = JSONObject.parseObject(AesEncryptUtil.aesCbcPkcs5PaddingDecrypt(decode));
            if (Objects.isNull(obj)) {
                return null;
            }
            if (!StringUtils.equals(obj.getString("typ"), "JWT")
                    || !StringUtils.equals(obj.getString("project"), "tianyicloud")) {
                return null;
            }

            return obj.getLong(CoreConstants.REQUEST_ATTR_USERID);//拿到用户id

        } catch (Exception e) {
            log.info("认证失败");
            e.printStackTrace();
            return null;
        }

    }

}
