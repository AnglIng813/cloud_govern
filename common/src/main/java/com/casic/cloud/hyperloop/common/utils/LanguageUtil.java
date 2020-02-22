package com.casic.cloud.hyperloop.common.utils;

import com.casic.cloud.hyperloop.common.constant.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/***
 * @Description:语言工具类
 * @TODO:
 * @Author: CM
 * @Date: 2019/7/2 15:25
 * @version: V1.0
*/
public class LanguageUtil {


    /***
     * @Description: 获取请求语言类型
     * @TODO: 
     * @Author: LDC
     * @Date: 2019/8/16 15:29
     * @version: V1.0
    */
    public static String getLanguage(){

        String language = SystemConstant.LanguageConstant.LANGUAGE_TYPE_ZH_EN;
        HttpServletRequest request = LanguageUtil.getRequest();
        if(Objects.isNull(request) && StringUtils.isNotBlank("language")){
            language = request.getHeader("language");
        }

        return language;
    }

    /***
     * @Description: 获取当前请求的 HttpServletRequest
     * @TODO:
     * @Author: LDC
     * @Date: 2019/8/16 15:42
     * @version: V1.0
    */
    public static HttpServletRequest getRequest(){

        HttpServletRequest request = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null){

            request = ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        return request;
    }
}
