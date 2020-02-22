package com.casic.cloud.hyperloop.common.model.result;

import com.casic.cloud.hyperloop.common.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public enum ApiErrorCode {

    /**
     * 成功
     */
    success(0, "成功", "success"),

    no_login_web(401, "未登录", "No Login"),

    /**
     * http错误
     */
    bad_gateway(502, "网关错误", "Bad Gateway"),

    /**
     * 1001-1300 校验等
     */
    parameter_missing(1001, "缺少参数/参数为空", "Missing parameter/Parameter is empty"),

    validation_error(1002, "验证码不正确,请重新输入", "The validation code is incorrect.Please re-enter it"),
    failed_password(1003, "密码输入有误", "The failed password was added incorrectly"),
    account_not_exists(1004, "用户不存在", "Account does not exist."),
    account_disabled(1005, "该用户已被禁用", "Account is disabled"),
    account_frozen(1006, "账户已冻结", "The account has been frozen"),
    validate_login_error(1007, "账户不存在或密码错误", "Account does not exist or password error"),
    field_length_overflow(1008, "参数验证未通过", "Parameter validation failed"),
    update_password_error(1009, "修改密码失败，原始密码输入有误", "Failed to modify the password, the original password input error"),
    modify_data_not_exist(1010, "修改的数据不存在", "Modify data not exist"),
    userid_empty(1011, "用户ID为空", "User ID is Empty"),
    unheld_character_coding(1012, "字符编码不支持", "Unheld Character Coding"),
    data_reading_writing_failure(1013, "数据读写失败", "Data Reading And Writing Failure"),
    alarm_name_is_exist(1014,"报警组名称已经存在","Alarm Name Is Exist"),
    parsing_yaml_failure(1015,"解析YAML文件失败","Parsing the yaml file failure"),
    parsing_yaml_to_json_failure(1016,"YAML文件转JSON失败","Parsing the yaml file to json failure"),


    /**
     * 1301-1400 数据库操作
     */
    query_failure(1301, "查询操作失败", "Query operation failed"),
    insert_failure(1302, "插入记录操作失败", "The insert record operation failed"),
    update_failure(1303, "更新记录操作失败", "The update record operation failed"),
    delete_failure(1304, "删除记录操作失败", "Deletion of records failed"),

    /**
     * 1401-1500 Cache操作
     */
    save_cache_failed(1401, "保存至cache失败", "Save to cache failed"),
    get_cache_failed(1402, "获取cache内容失败", "Failed to get cache content"),


    /**
     * 1501-1600 第三方操作错误码
     */
    oss_call_back_failure(1501, "OSS回调失败", "OSS callback failed"),
    remote_oms_service_call_failure(1502, "远程调用oms服务失败", "The remote oms service call failed"),

    /**
     * 1601-1700 流程相关
     */
    cannot_delete_running_process(1601,"不能删除正在运行的流程","Cannot delete the running process"),
    user_does_not_have_current_permissions(1602,"该用户不具备当前权限","The user does not have the current permissions"),
    process_not_exist(1603,"流程不存在","The process is not exist"),

    /**
     * 2001-2500  身份权限相关
     */
    permissionid_empty(2001, "权限ID为空", "Permission ID is Empty."),

    /**
     * 系统级错误 9001-9999
     */
    api_sign_contetn_type_error(9001, "不支持的content-type类型", "Unsupported content-type type"),
    api_sign_parameter_missing(9002, "缺少签名参数", "Missing signature parameter"),
    api_sign_fail(9003, "验签失败", "Attestation of failure"),
    api_sign_application_close(9004, "应用已停用", "Application disabled"),
    api_sign_public_key_illegal(9005, "非法公钥", "The public key of illegal"),
    api_sign_application_not_exists(9006, "应用不存在", "Application does not exist"),


    /**
     * 服务器异常
     */
    server_exception(9999, "服务器异常", "Server Exception");


    private int code;
    private String zh_message;
    private String en_message;

    private ApiErrorCode(int code, String zh_message, String en_message) {
        this.code = code;
        this.zh_message = zh_message;
        this.en_message = en_message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(String language) {

        if (StringUtils.isNotEmpty(language) && SystemConstant.LanguageConstant.LANGUAGE_TYPE_EN_US.equals(language)) {
            return en_message;
        } else {
            return zh_message;
        }
    }


}
