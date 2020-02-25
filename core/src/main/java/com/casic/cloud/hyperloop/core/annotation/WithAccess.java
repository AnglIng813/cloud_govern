package com.casic.cloud.hyperloop.core.annotation;

import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

/**
 * @Description: 此注解用来标记方法参数
 * @Author: AnglIng
 * @Date: 2020/2/24 13:58
 * @version: V1.0
 *
 *~  @Documented 标记编译时打包到class
 *~  @Target({ElementType.PARAMETER, ElementType.METHOD}) 声明自定义的注解作用域
 *~  @Retention(RetentionPolicy.RUNTIME) 注解保留策略，不仅被保存到class文件中，
 *   jvm加载class文件之后，仍然存在
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithAccess {

    boolean required() default true;

    ApiErrorCode apiErrorCode() default ApiErrorCode.no_login_web;

}
