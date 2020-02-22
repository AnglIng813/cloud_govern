package com.casic.cloud.hyperloop.common.exception.base;

import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.model.result.ApiResult;
import com.casic.cloud.hyperloop.common.utils.LanguageUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:自定义异常异常类
 * @TODO:
 * @Author: LDC
 * @Date: 2019/8/16
 * @version: V1.0
 **/
@Data
public class BaseException extends RuntimeException {

    public int code = 9999;

    public String message;

    public String extraMessage;

    public ApiErrorCode apiErrorCode;

    public BaseException(){
        super();
    }

    /**
     * @Description: 构建自定义异常信息
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 21:00
     * @version: V1.0
     */
    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @Description: 构建异常-标准错误码、错误信息
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 21:00
     * @version: V1.0
     */
    public BaseException(ApiErrorCode apiErrorCode) {
        this.apiErrorCode = apiErrorCode;
    }

    /**
     * @Description: 构建异常-标准错误码、错误信息
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 21:00
     * @version: V1.0
     */
    public BaseException(String extraMessage,ApiErrorCode apiErrorCode) {
        this.apiErrorCode = apiErrorCode;
        this.extraMessage = extraMessage;
    }

    /**
     * @Description: 获取相应的ApiResult结果集信息
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 10:58
     * @version: V1.0
     **/
    public ApiResult getApiResult(){

        String extra ="";
        if(StringUtils.isNotEmpty(this.extraMessage)){
            extra = this.extraMessage+"->";
        }

        if(this.apiErrorCode!=null){
            return new ApiResult(apiErrorCode.getCode(),extra + apiErrorCode.getMessage(LanguageUtil.getLanguage()),null);
        }else{
            return new ApiResult(this.code,extra + this.message,null);
        }

    }
}
