package com.casic.cloud.hyperloop.common.exception;

import com.casic.cloud.hyperloop.common.exception.base.BaseException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;

/**
 * @Description:cache-server异常类
 **/
public class CloudApiServerException extends BaseException {

    public CloudApiServerException(){super();}

    public CloudApiServerException(int code, String message) {
        super(code, message);
    }

    public CloudApiServerException(ApiErrorCode apiErrorCode) {
        super(apiErrorCode);
    }

    public CloudApiServerException(String extraMessage,ApiErrorCode apiErrorCode) {
        super(extraMessage,apiErrorCode);
    }

}
