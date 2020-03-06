package com.casic.cloud.hyperloop.core.enums;

import com.casic.cloud.hyperloop.common.enums.CodeEnum;

/**
 * 日志枚举类
 *
 * @author lvbaolin
 */

public enum OperationEnum
        implements CodeEnum
{

    ADD("add", 1001, "新增", 1),
    SELECT("select", 1002, "查询", 1),
    INSERT("insert", 1003, "新增", 1),
    UPDATE("uodate", 1004, "更新", 1),
    LOGIN("login", 1005, "新增", 1);

    private String value;
    private Integer code;
    private String description;
    private Integer state;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Integer getCode() {
        return code;
    }


    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getState() {
        return state;
    }


    OperationEnum(String value, Integer code, String description, Integer state) {
        this.value = value;
        this.code = code;
        this.description = description;
        this.state = state;


    }


}
