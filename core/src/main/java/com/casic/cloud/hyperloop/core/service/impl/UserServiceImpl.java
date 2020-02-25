package com.casic.cloud.hyperloop.core.service.impl;

import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.utils.ConvertBeanUtils;
import com.casic.cloud.hyperloop.core.dao.UserMapper;
import com.casic.cloud.hyperloop.core.model.domain.User;
import com.casic.cloud.hyperloop.core.model.dto.UserDTO;
import com.casic.cloud.hyperloop.core.model.result.UserRes;
import com.casic.cloud.hyperloop.core.service.UserService;
import com.github.pagehelper.PageHelper;
import com.sun.tools.javac.util.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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


    private Date convert2Time(Long createDate) {
        return new Date(createDate);
    }
}
