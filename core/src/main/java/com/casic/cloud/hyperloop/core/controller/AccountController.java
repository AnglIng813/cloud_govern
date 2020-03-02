package com.casic.cloud.hyperloop.core.controller;

import com.casic.cloud.hyperloop.common.model.result.ApiResult;
import com.casic.cloud.hyperloop.common.utils.ConvertBeanUtils;
import com.casic.cloud.hyperloop.core.annotation.WithAccess;
import com.casic.cloud.hyperloop.core.constants.UrlMapping;
import com.casic.cloud.hyperloop.core.model.domain.User;
import com.casic.cloud.hyperloop.core.model.dto.AccountDTO;
import com.casic.cloud.hyperloop.core.model.vo.AccountVO;
import com.casic.cloud.hyperloop.core.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description:用户相关接口
 * @Author: LDC
 * @Date: 2020/02/20 13:17
 * @version: V1.0
 */
@Slf4j
@RestController
@Api(tags = {"AccountController"}, value = "用户相关接口")
public class AccountController {

    @Autowired
    private AccountService accountService;



    /***
     * @Description:用户登录
     * @Author: LDC
     * @Date: 2020/2/20 13:18
     */
    @ApiOperation(value = "用户登录-GET请求", httpMethod = "GET")
    @GetMapping(UrlMapping.SININ)
    public ApiResult login(@ApiIgnore @WithAccess User user) {

        //测试WithAccess
        log.info("【测试WithAccess】,userName={}", user.getUserName());

        //TODO 自定义参数解析器 自定义日志注解
        //拉取菜单信息
        //记录最后登录时间
        //log.info("【用户登录】成功，userId", userId);
        return ApiResult.addSuccess();
    }

    /**
     * @Description:临时获取token接口-POST请求
     * @Author: AnglIng
     * @Date: 2020/3/2 9:42
     */
    @ApiOperation(value = "临时获取token接口-POST请求", httpMethod = "POST")
    @PostMapping(UrlMapping.TEMPORARY_TOKEN)
    public ApiResult login(@RequestBody AccountVO vo) {
        //校验，生成token返回前端
        String token = accountService.validate(ConvertBeanUtils.converBean2Bean(vo, AccountDTO.class));
        log.info("【临时获取token接口】成功,userName={}", vo.getUserName());
        return ApiResult.addSuccess(token);
    }

}
