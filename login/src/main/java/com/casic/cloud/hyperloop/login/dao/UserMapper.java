package com.casic.cloud.hyperloop.login.dao;

import com.casic.cloud.hyperloop.login.model.domain.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByCondition(User record);
}