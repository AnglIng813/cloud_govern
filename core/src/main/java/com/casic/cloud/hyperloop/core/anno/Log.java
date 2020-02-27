package com.casic.cloud.hyperloop.core.anno;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {


    /**
     * 模块
     *
     * @return
     */
    String module() default "";


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
    String operationType() default "";

}