package com.casic.cloud.hyperloop.service.api.service.impl;

import com.casic.cloud.hyperloop.service.api.okhttp.OkHttpUtils;
import com.casic.cloud.hyperloop.service.api.service.OkHttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description: 待完善
 * @Author: LDC
 * @Date: 2020/01/03 11:04
 * @version: V1.0
 */
@Slf4j
@Service
public class OkHttpServiceImpl implements OkHttpService {
    @Override
    public String syncGet() {
        // get请求，方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        return OkHttpUtils.builder().url("请求地址，http/https都可以")
                // 有参数的话添加参数，可多个
                .addParam("参数名", "参数值")
                .addParam("参数名", "参数值")
                // 也可以添加多个
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .get()
                // 可选择是同步请求还是异步请求
                //.async();
                .sync();
    }

    @Override
    public String asyncGet() {
        // get请求，方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        return OkHttpUtils.builder().url("请求地址，http/https都可以")
                // 有参数的话添加参数，可多个
                .addParam("参数名", "参数值")
                .addParam("参数名", "参数值")
                // 也可以添加多个
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .get()
                // 可选择是同步请求还是异步请求
                .async();
    }

    @Override
    public String postForm() {
        // post请求，分为两种，一种是普通表单提交，一种是json提交
        return OkHttpUtils.builder().url("请求地址，http/https都可以")
                // 有参数的话添加参数，可多个
                .addParam("参数名", "参数值")
                .addParam("参数名", "参数值")
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(true)
                .sync();
    }

    @Override
    public String postJson() {
        // post请求，分为两种，一种是普通表单提交，一种是json提交
        return OkHttpUtils.builder().url("请求地址，http/https都可以")
                // 有参数的话添加参数，可多个
                .addParam("参数名", "参数值")
                .addParam("参数名", "参数值")
                // 也可以添加多个
                .addHeader("Content-Type", "application/json; charset=utf-8")
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(false)
                .sync();
    }
}
