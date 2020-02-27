package com.casic.cloud.hyperloop.core.controller;

import com.casic.cloud.hyperloop.core.annotation.LogCustom;
import com.casic.cloud.hyperloop.core.annotation.OperationEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = {"LogController"}, value = "日志测试")
public class LogController {

    @ApiOperation(value = "hello接口", httpMethod = "GET")
    @LogCustom(module = "日志模块", operation = "日志测试", description = "核心业务描述", operationType = OperationEnum.SELECT)
    @GetMapping("hello")
    public String hello() {

        return "hello world";
    }
}
