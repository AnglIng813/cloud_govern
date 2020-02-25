package com.casic.cloud.hyperloop.core.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private String userName;

    /**
     * 
     */
    private String showName;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private Long departmentId;

    /**
     * 
     */
    private String departmentName;

    /**
     * 
     */
    private Long authId;

    /**
     * 
     */
    private Long loginId;

    /**
     * 
     */
    private String roleName;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String telephone;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private Long loginStatus;

    /**
     * 
     */
    private Long status;

    /**
     * 
     */
    private Date createDate;

    /**
     * 
     */
    private Date modifyDate;

    /**
     * 
     */
    private Date lastloginDate;

    /**
     * 
     */
    private String signId;

    /**
     * 
     */
    private String authIds;

    /**
     * 
     */
    private Boolean departmentAdmin;

    /**
     * 登陆失败次数
     */
    private Integer loginFailureTimes;

    /**
     * 重置密码时间
     */
    private Date resetPwdDate;

    /**
     * 账户剩余冻结时间
     */
    private String frozenTimes;

    /**
     * 账户冻结日期
     */
    private Date frozenDate;

    /**
     * 租户命名空间
     */
    private String namespace;

}