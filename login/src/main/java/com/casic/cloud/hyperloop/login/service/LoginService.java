package com.casic.cloud.hyperloop.login.service;

import com.casic.cloud.hyperloop.login.model.dto.LoginDTO;

public interface LoginService {
    Long validate(LoginDTO loginDTO);

    String convertToken(String callBack, Long userId);
}
