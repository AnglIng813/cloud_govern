package com.casic.cloud.hyperloop.core.controller;

import com.casic.cloud.hyperloop.core.annotation.LogCustom;
import com.casic.cloud.hyperloop.core.constants.OperationConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = {"LogController"}, value = "日志测试")
public class LogController {

    @ApiOperation(value = "hello接口", httpMethod = "GET")
    @LogCustom(module = "日志模块", operation = "日志测试", operationType = OperationConstants.DELETE)
    @GetMapping("hello")
    public String hello() {
        return "hello world";
    }
}
