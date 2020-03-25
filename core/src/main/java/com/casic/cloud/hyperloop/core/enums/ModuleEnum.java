package com.casic.cloud.hyperloop.core.enums;

import com.casic.cloud.hyperloop.common.enums.CodeEnum;

public enum ModuleEnum implements CodeEnum {

    test(0, "test", "测试"),
    ;

    private Integer code;//代码
    private String value;//模块
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

    ModuleEnum(Integer code, String value, String description) {
        this.code = code;
        this.value = value;
        this.description = description;
    }
}
