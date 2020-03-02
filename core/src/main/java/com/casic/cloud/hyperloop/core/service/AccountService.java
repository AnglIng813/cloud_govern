package com.casic.cloud.hyperloop.core.service;

import com.casic.cloud.hyperloop.core.model.dto.AccountDTO;
import com.casic.cloud.hyperloop.core.model.dto.UserDTO;
import com.casic.cloud.hyperloop.core.model.result.UserRes;

import java.util.List;

public interface AccountService {
    UserRes selectByCondition(UserDTO dto);

    List<UserRes> selectUserListByCondition(UserDTO dto);

    String validate(AccountDTO dto);

}
