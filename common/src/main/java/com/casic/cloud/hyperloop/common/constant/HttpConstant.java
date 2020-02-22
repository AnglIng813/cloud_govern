package com.casic.cloud.hyperloop.common.constant;

/**
 * @Description: http相关常量类
 * @Author: LDC
 * @Date: 2019/8/16
 * @version: V1.0
*/
public class HttpConstant {


    /**
     * mime类型常量类
     */
    public static final class MIMETypeConstant{

        /**
         * Content-Type: application/json
         */
        public static final String APPLICATION_JSON = "application/json";

        /**
         * Content-Type: multipart/form-data
         */
        public static final String MULTUPART_FORMDATA = "multipart/form-data";
        /**
         * Content-Type: multipart/form-data
         */
        public static final String APPLICATION_X_WWW_FORM = "application/x-www-form-urlencoded";

        public static final String APPLICATION_GZIP = "application/gzip";

        public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    }

    /**
     * 请求协议常量类
     */
    public static final  class HttpMethodConstant{

        /**
         * 请求协议：GET
         */
        public static final String HTTP_METHOD_GET = "GET";

        /**
         * 请求协议：POST
         */
        public static final String HTTP_METHOD_POST = "POST";
        /**
         * 请求协议：POST
         */
        public static final String HTTP_METHOD_PUT = "PUT";

        /**
         * 请求协议：POST
         */
        public static final String HTTP_METHOD_DELETE = "DELETE";

    }

}
