package com.casic.cloud.hyperloop.core.resolver;

import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.utils.AuthorizationUtil;
import com.casic.cloud.hyperloop.common.utils.ConvertBeanUtils;
import com.casic.cloud.hyperloop.core.annotation.WithAccess;
import com.casic.cloud.hyperloop.core.constants.CoreConstants;
import com.casic.cloud.hyperloop.core.model.domain.User;
import com.casic.cloud.hyperloop.core.model.dto.UserDTO;
import com.casic.cloud.hyperloop.core.model.result.UserRes;
import com.casic.cloud.hyperloop.core.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Description:
 * @Author: AnglIng
 * @Date: 2020/2/25 14:15
 * @version: V1.0
 */
@Slf4j
@Component
public class WithAccessMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //此处特别注意，我们使用的是参数注解
        return methodParameter.hasParameterAnnotation(WithAccess.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //没打注解，不需要处理
        WithAccess withAccess = methodParameter.getParameterAnnotation(WithAccess.class);
        if (Objects.isNull(withAccess)) {
            return null;
        }

        //打了注解且开启了认证
        if (withAccess.required()) {
            //认证失败抛401
            HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            Long userId = AuthorizationUtil.authorization(request, CoreConstants.REQUEST_ATTR_USERID);
            if (Objects.isNull(userId)) {
                throw new CloudApiServerException(withAccess.apiErrorCode());
            }

            //返回用户
            if (methodParameter.getParameterType().equals(User.class)) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(userId);
                UserRes res = accountService.selectByCondition(userDTO);
                return ConvertBeanUtils.converBean2Bean(res, User.class);
            }

        }

        return null;
    }


}
