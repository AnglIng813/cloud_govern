package com.casic.cloud.hyperloop.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.utils.AesEncryptUtil;
import com.casic.cloud.hyperloop.common.utils.ConvertBeanUtils;
import com.casic.cloud.hyperloop.common.utils.SM3Utils;
import com.casic.cloud.hyperloop.core.dao.UserMapper;
import com.casic.cloud.hyperloop.core.model.domain.User;
import com.casic.cloud.hyperloop.core.model.dto.AccountDTO;
import com.casic.cloud.hyperloop.core.model.dto.UserDTO;
import com.casic.cloud.hyperloop.core.model.result.UserRes;
import com.casic.cloud.hyperloop.core.service.AccountService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @Author: LDC
 * @Date: 2020/01/03 11:04
 * @version: V1.0
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional(readOnly = true)
    public UserRes selectByCondition(UserDTO dto) {
        //拷贝内容
        User user = ConvertBeanUtils.converBean2Bean(dto, User.class);
        UserRes userRes = null;
        try {
            userRes = userMapper.selectByCondition(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【根据条件查询user信息】失败,userId={},userName={}", dto.getUserId(), dto.getUserName());
            throw new CloudApiServerException(ApiErrorCode.query_failure);
        }

        return userRes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRes> selectUserListByCondition(UserDTO dto) {
        //设置分页
        Integer pageNum = dto.getPageNum() == null ? 1 : dto.getPageNum();
        Integer pageSize = dto.getPageSize() == null ? 20 : dto.getPageSize();
        PageHelper.startPage(pageNum, pageSize);

        //拷贝内容
        User user = ConvertBeanUtils.converBean2Bean(dto, User.class);

        /*如有日期类型 参考如下方式
        user.setCreateDate(this.convert2Time(dto.getCreateDate()));*/

        List<UserRes> resList = null;
        try {
            resList = userMapper.selectUserListByCondition(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【根据条件查询user列表】失败,userId={},userName={}", dto.getUserId(), dto.getUserName());
            throw new CloudApiServerException(ApiErrorCode.query_failure);
        }

        return resList;
    }

    @Override
    public String validate(AccountDTO dto) {
        //获取user
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(dto.getUserName());
        UserRes res = this.selectByCondition(userDTO);
        if (Objects.isNull(res)) {
            log.error("【用户校验】用户不存在");
            throw new CloudApiServerException(ApiErrorCode.account_not_exists);
        }

        //密码
        if (!SM3Utils.verifyWithKey(dto.getPassword(), res.getPassword())) {
            log.error("【密码校验】密码错误");
            throw new CloudApiServerException(ApiErrorCode.account_not_exists);
        }

        return this.buildToken(res.getUserId());
    }


    private Date convert2Time(Long createDate) {
        return new Date(createDate);
    }

    private String buildToken(Long userId) {
        JSONObject object = new JSONObject();
        //模拟jwt
        object.put("typ", "JWT");
        object.put("project", "tianyicloud");
        object.put("userId", userId);
        return AesEncryptUtil.aesCbcPkcs5PaddingEncrypt(object.toJSONString());
    }
}
