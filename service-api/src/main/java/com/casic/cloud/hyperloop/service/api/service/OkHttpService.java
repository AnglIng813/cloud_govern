package com.casic.cloud.hyperloop.service.api.service;

public interface OkHttpService {
    /**
     * 同步get请求
     */
    String syncGet();

    /**
     * 异步get请求
     */
    String asyncGet();

    /**
     * 表单提交 content-type
     * @return
     */
    String postForm();

    String postJson();
}
