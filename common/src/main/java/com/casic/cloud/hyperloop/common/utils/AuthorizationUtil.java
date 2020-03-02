package com.casic.cloud.hyperloop.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * @Description:
 * @Author: AnglIng
 * @Date: 2020/3/2 15:43
 * @version: V1.0
 */
@Slf4j
public class AuthorizationUtil {


    public static Long authorization(HttpServletRequest request, String TKey) {
        String confusionStr = "Hyperloop ";
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization) || !authorization.startsWith(confusionStr)) {
            return null;
        }
        String token = authorization.substring(confusionStr.length());
        if (StringUtils.isEmpty(token) || StringUtils.equalsIgnoreCase("undefined", token)) {
            return null;
        }
        //Url转换
        String decode = convertUrl(token);
        if (Objects.isNull(decode)) {
            return null;
        }
        //解密
        JSONObject obj = JSONObject.parseObject(AesEncryptUtil.aesCbcPkcs5PaddingDecrypt(decode));
        if (Objects.isNull(obj)) {
            return null;
        }
        if (!StringUtils.equals(obj.getString("typ"), "JWT")
                || !StringUtils.equals(obj.getString("project"), "tianyicloud")) {
            return null;
        }

        return obj.getLong(TKey);//拿到用户id
    }

    public static String convertUrl(String token) {
        try {
            /**
             * 由于使用了Aes加密，token中会出现"+"号，故此处指判断"%"号
             * TODO 待优化
             */
            if(StringUtils.contains(token , "%")){
                token = URLDecoder.decode(token, System.getProperty("file.encoding"));
            }
            return token;
        } catch (UnsupportedEncodingException e) {
            log.info("认证失败");
            e.printStackTrace();
            return null;
        }
    }
}
