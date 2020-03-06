package com.casic.cloud.hyperloop.core.annotation;

import com.casic.cloud.hyperloop.core.enums.OperationEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogCustom {


    /**
     * 模块
     *
     * @return
     */
    String  module () default "";


    /**
     * 操作
     *
     * @return
     */
    String operation() default "";

    /**
     * 操作类型
     *
     * @return
     */
    OperationEnum operationType() default OperationEnum.ADD;


    /**
     * @return
     */
    String description() default "";




}