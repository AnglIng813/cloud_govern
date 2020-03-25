package com.casic.cloud.hyperloop.core.annotation;

import com.casic.cloud.hyperloop.core.enums.ModuleEnum;
import com.casic.cloud.hyperloop.core.enums.OperationEnum;
import org.apache.ibatis.jdbc.Null;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogCustom {

    /**
     * 模块
     */
    ModuleEnum module () default ModuleEnum.test;

    /**
     * 操作类型
     */
    OperationEnum type() default OperationEnum.QUERY;

    /**
     * 描述
     */
    String description() default "查询";




}