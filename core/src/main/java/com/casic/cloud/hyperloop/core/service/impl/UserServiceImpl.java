package com.casic.cloud.hyperloop.core.service.impl;

import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.utils.ConvertBeanUtils;
import com.casic.cloud.hyperloop.core.dao.UserMapper;
import com.casic.cloud.hyperloop.core.model.domain.User;
import com.casic.cloud.hyperloop.core.model.dto.UserDTO;
import com.casic.cloud.hyperloop.core.model.result.UserRes;
import com.casic.cloud.hyperloop.core.service.UserService;
import com.sun.tools.javac.util.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description:
 * @Author: LDC
 * @Date: 2020/01/03 11:04
 * @version: V1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserRes selectByCondition(UserDTO dto) {
        //拷贝内容
        User user = ConvertBeanUtils.converBean2Bean(dto, User.class);
        user.setCreateDate(this.convert2Time(dto.getCreateDate()));
        user.setModifyDate(this.convert2Time(dto.getModifyDate()));
        user.setFrozenDate(this.convert2Time(dto.getFrozenDate()));
        user.setResetPwdDate(this.convert2Time(dto.getResetPwdDate()));
        user.setLastloginDate(this.convert2Time(dto.getLastloginDate()));

        UserRes userRes = null;
        try {
            userRes = userMapper.selectByCondition(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【根据条件查询user】失败,dto={}", dto);
            throw new CloudApiServerException(ApiErrorCode.query_failure);
        }

        return userRes;
    }

    private Date convert2Time(Long createDate) {
        return new Date(createDate);
    }
}
