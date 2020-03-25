package com.casic.cloud.hyperloop.core.enums;

import com.casic.cloud.hyperloop.common.enums.CodeEnum;

/**
 * 日志枚举类
 *
 * @author lvbaolin
 */

public enum OperationEnum implements CodeEnum {

    QUERY(0, "query", "查询"),
    INSERT(1, "insert", "新增"),
    UPDATE(2, "uodate", "修改"),
    DELETE(3, "delete", "删除"),
    ;

    private Integer code;//代码
    private String value;//操作
    private String description;//描述

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    OperationEnum(Integer code, String value, String description) {
        this.code = code;
        this.value = value;
        this.description = description;
    }
}
