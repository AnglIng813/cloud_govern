package com.casic.cloud.hyperloop.common.handler;

import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.exception.base.BaseException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.model.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:全局异常处理器
 * @TODO:
 * @Author: LDC
 * @Date: 2019/8/16
 * @version: V1.0
 */

@ControllerAdvice
@ResponseBody//必须添加，否则Spring会将返回值当作视图解析抛出异常
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    HttpServletRequest httpServletRequest;

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class, BaseException.class})
    public Object MethodArgumentNotValidHandler(Object exception) {
        //按需重新封装需要返回的错误信息
        if (exception instanceof BaseException) {
            BaseException baseException = (BaseException) exception;
            log.info("【GlobalExceptionHandler】异常类型，BaseException");
            return baseException.getApiResult();
        }

        BindingResult bindingResult = null;
        if (exception instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            log.info("【验证框架】异常类型，MethodArgumentNotValidException");
        } else if (exception instanceof BindException) {
            exception = (BindException) exception;
            bindingResult = ((BindException) exception).getBindingResult();
            log.info("【验证框架】异常类型，BindException");
        }

        //解析原错误信息，封装后返回，此处返回非法的字段名称，错误信息
        //自定义错误码
        int errorCode = ApiErrorCode.field_length_overflow.getCode();
        //错误字段
        String errorField = bindingResult.getFieldError().getField();
        //错误提示信息
        String errorMsg = bindingResult.getFieldError().getDefaultMessage();
        //内容
        String errorValue = (String) bindingResult.getFieldError().getRejectedValue();

        return ApiResult.addFail(errorCode, errorField + "-->>" + errorMsg, null);
    }

}
