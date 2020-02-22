package com.casic.cloud.hyperloop.common.model.result;

import com.casic.cloud.hyperloop.common.utils.LanguageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: API接口响应基类
 * @TODO:
 * @Author: LDC
 * @Date: 2019/8/16 11:25
 **/
@Data
@ApiModel(value = "响应信息model")
@AllArgsConstructor
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1199872738772265178L;

    @ApiModelProperty(value = "状态码", required = true)
    private int code;

    @ApiModelProperty(value = "提示信息", required = true)
    private String message;

    @ApiModelProperty("数据项")
    private T response;


    public ApiResult() {
        super();
    }




    /**
     * @Description: 生成成功结果集，并填充数据项-response
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:46
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addSuccess(T response) {

        ApiResult<T> apiResult = new ApiResult<T>(ApiErrorCode.success.getCode(), ApiErrorCode.success.getMessage(LanguageUtil.getLanguage()), response);
        return apiResult;
    }

    public static <T> ApiResult<T> addSuccess(ApiErrorCode apiErrorCode) {

        ApiResult<T> apiResult = new ApiResult<T>(apiErrorCode.getCode(), apiErrorCode.getMessage(LanguageUtil.getLanguage()), null);
        return apiResult;
    }

    /**
     * @Description: 生成成功结果集，并填充数据项
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:46
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addSuccess() {

        ApiResult<T> apiResult = new ApiResult<T>(ApiErrorCode.success.getCode(), ApiErrorCode.success.getMessage(LanguageUtil.getLanguage()), null);
        return apiResult;
    }

    /**
     * @Description: 生成成功结果集，并填充数据项-response
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:46
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addSuccess(ApiErrorCode apiErrorCode, T response) {

        ApiResult<T> apiResult = new ApiResult<T>(apiErrorCode.getCode(), apiErrorCode.getMessage(LanguageUtil.getLanguage()), response);
        return apiResult;
    }

    /**
     * @Description: 生成失败结果集，填充状态码和错误信息
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:50
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addFail(int code, String message, T response) {

        ApiResult<T> apiResult = new ApiResult<T>(code, message, response);
        return apiResult;
    }

    /**
     * @Description: 生成失败结果集，填充状态码和错误信息
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:50
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addFail(ApiErrorCode apiErrorCode) {

        ApiResult<T> apiResult = new ApiResult<T>(apiErrorCode.getCode(), apiErrorCode.getMessage(LanguageUtil.getLanguage()), null);
        return apiResult;
    }

    /**
     * @Description: 生成失败结果集，填充状态码和错误信息
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:50
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addFail(ApiErrorCode apiErrorCode, String extraMsg) {

        ApiResult<T> apiResult = new ApiResult<T>(apiErrorCode.getCode(), apiErrorCode.getMessage(LanguageUtil.getLanguage()) + extraMsg, null);
        return apiResult;
    }

    /**
     * @Description: 生成失败结果集-添加服务器异常
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:50
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addFail() {

        ApiResult<T> apiResult = new ApiResult<T>(ApiErrorCode.server_exception.getCode(), ApiErrorCode.server_exception.getMessage(LanguageUtil.getLanguage()), null);
        return apiResult;
    }

    /**
     * @Description: 生成成功结果集-填充分页数据项
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:50
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addSuccessPage(Integer pageNum, Integer pageSize, Integer pages, Long total, List datas) {

        //填充分页项
        PageResult<T> response = new PageResult<T>(pageNum, pageSize, pages, total, datas);
        ApiResult<T> apiResult = new ApiResult<T>(ApiErrorCode.success.getCode(), ApiErrorCode.success.getMessage(LanguageUtil.getLanguage()), (T) response);
        return apiResult;
    }

    /**
     * @Description: 生成成功结果集-填充分页数据项
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:50
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addSuccessPage(Page datas) {

        //填充分页项
        PageResult<T> response = null;
        if (datas != null) {
            response = new PageResult<T>(datas.getPageNum(), datas.getPageSize(), datas.getPages(), datas.getTotal(), datas);
        }

        ApiResult<T> apiResult = new ApiResult<T>(ApiErrorCode.success.getCode(), ApiErrorCode.success.getMessage(LanguageUtil.getLanguage()), (T) response);
        return apiResult;
    }

    /**
     * @Description: 生成成功结果集-填充分页数据项
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 11:50
     * @version: V1.0
     **/
    public static <T> ApiResult<T> addSuccessPage(PageInfo datas) {

        //填充分页项
        PageResult<T> response = null;
        if (datas != null) {
            response = new PageResult<T>(datas.getPageNum(), datas.getPageSize(), datas.getPages(), datas.getTotal(), datas.getList());
        }

        ApiResult<T> apiResult = new ApiResult<T>(ApiErrorCode.success.getCode(), ApiErrorCode.success.getMessage(LanguageUtil.getLanguage()), (T) response);
        return apiResult;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }
}
