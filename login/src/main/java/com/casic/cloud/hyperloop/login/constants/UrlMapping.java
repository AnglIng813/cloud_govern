package com.casic.cloud.hyperloop.login.constants;


/**
 * URL地址常量类
 */
public class UrlMapping {

    /** base */
    public static final String BASE = "/api/v1";
    /** 登录相关 */
    public static final String LOGINS = "/login";
    /** 重定向接口 */
    public static final String REDIRECT = BASE + LOGINS + "/redirect";
    /** 登出接口 */
    public static final String LOGOUT = BASE + LOGINS + "/logout";


//    /**
//     * 根据模块区分 restful风格
//     *
//     *  例

//    /** 港口相关 */
//    public static final String PORTS = "/ports";
//    /** 添加自定义港口信息 */
//    public static final String ADD_PROT = BASE + PORTS + "/port";
//    /** 修改自定义港口信息 */
//    public static final String MODIFY_PROT = BASE + PORTS + "/port";
//    /** 删除自定义港口信息 */
//    public static final String DEL_PROT = BASE + PORTS + "/delPort";
//    /** 查看自定义港口列表 */
//    public static final String PROT_LIST = BASE + PORTS + "/ports";
//    /** 查看自定义港口详情 */
//    public static final String PROT_INFO = BASE + PORTS + "/port";

}
