package com.casic.cloud.hyperloop.core.controller;

import com.casic.cloud.hyperloop.core.annotation.LogCustom;
import com.casic.cloud.hyperloop.core.enums.ModuleEnum;
import com.casic.cloud.hyperloop.core.enums.OperationEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 自定义日志注解测试
 *
 * @author lvbaolin
 */
@Slf4j
@RestController
@Api(tags = {"LogController"}, value = "日志测试")
public class LogController {

    @ApiOperation(value = "hello接口", httpMethod = "GET")
    @LogCustom(module = ModuleEnum.test, type = OperationEnum.QUERY, description = "查看")
    @GetMapping("/api/v1/hello")
    public String hello() {

        return "hello world";
    }
}
