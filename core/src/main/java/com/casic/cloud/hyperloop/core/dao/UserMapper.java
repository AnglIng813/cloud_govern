package com.casic.cloud.hyperloop.core.dao;

import com.casic.cloud.hyperloop.core.model.domain.User;
import com.casic.cloud.hyperloop.core.model.result.UserRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    UserRes selectByCondition(User record);
}