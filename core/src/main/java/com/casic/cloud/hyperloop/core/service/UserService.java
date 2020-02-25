package com.casic.cloud.hyperloop.core.service;

import com.casic.cloud.hyperloop.core.model.domain.User;
import com.casic.cloud.hyperloop.core.model.dto.UserDTO;
import com.casic.cloud.hyperloop.core.model.result.UserRes;

public interface UserService {
    UserRes selectByCondition(UserDTO dto);
}
