package com.casic.cloud.hyperloop.login.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.utils.AesEncryptUtil;
import com.casic.cloud.hyperloop.common.utils.SM3Utils;
import com.casic.cloud.hyperloop.login.dao.UserMapper;
import com.casic.cloud.hyperloop.login.model.domain.User;
import com.casic.cloud.hyperloop.login.model.dto.LoginDTO;
import com.casic.cloud.hyperloop.login.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Description:
 * @Author: LDC
 * @Date: 2020/01/03 11:04
 * @version: V1.0
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Long validate(LoginDTO dto) {
        //验证码 目前放在session中，后面可放入redis
        this.validateCode(dto.getVerifyCode());

        //获取user
        User user = this.selectByCondition(dto);
        if (Objects.isNull(user)) {
            log.error("【用户校验】用户不存在");
            throw new CloudApiServerException(ApiErrorCode.account_not_exists);
        }

        //密码
        if (!SM3Utils.verifyWithKey(dto.getPassword(), user.getPassword())) {
            log.error("【密码校验】密码错误");
            throw new CloudApiServerException(ApiErrorCode.account_not_exists);
        }

        request.getSession().setAttribute(user.getUserId().toString(), this.buildToken(user.getUserId()));

        return user.getUserId();
    }

    private String buildToken(Long userId) {
        JSONObject object = new JSONObject();
        //模拟jwt
        object.put("typ", "JWT");
        object.put("project", "tianyicloud");
        object.put("userId", userId);
        return AesEncryptUtil.aesCbcPkcs5PaddingEncrypt(object.toJSONString());
    }

    private User selectByCondition(LoginDTO dto) {
        User user = new User();
        try {
            user.setUserName(dto.getUserName());
            user = userMapper.selectByCondition(user);
        } catch (Exception e) {
            log.error("【按条件查询用户】失败userName={}", dto.getUserName());
            e.printStackTrace();
            throw new CloudApiServerException(ApiErrorCode.query_failure);
        }
        return user;
    }

    private void validatePwd(String password, String dbPwd) {

    }

    private void validateCode(String verifyCode) {
        String session_verifyCode = (String) request.getSession().getAttribute("verifyCode");
        if (StringUtils.equalsIgnoreCase(verifyCode, session_verifyCode)) {
            log.error("【登录接口】验证码错误,verifyCode={}", verifyCode);
            throw new CloudApiServerException(ApiErrorCode.validation_error);
        }
    }


}
