package com.casic.cloud.hyperloop.core.annotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: AnglIng
 * @Date: 2020/2/24 13:58
 * @version: V1.0
 *
 *~  @Documented 标记编译时打包到class
 *~  @Target({ElementType.FIELD, ElementType.METHOD}) 声明自定义的注解作用域
 *~  @Retention(RetentionPolicy.RUNTIME) 注解保留策略，不仅被保存到class文件中，
 *   jvm加载class文件之后，仍然存在
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {


}
