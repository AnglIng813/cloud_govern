package com.casic.cloud.hyperloop.core.model.result;

import com.casic.cloud.hyperloop.common.model.result.BaseRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserRes extends BaseRes {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "显示名")
    private String showName;

    @ApiModelProperty(value = "组织机构id")
    private Long departmentId;

    @ApiModelProperty(value = "组织机构名")
    private String departmentName;

    @ApiModelProperty(value = "")
    private Long authId;

    @ApiModelProperty(value = "")
    private Long loginId;

    @ApiModelProperty(value = "")
    private String roleName;

    @ApiModelProperty(value = "")
    private String phone;

    @ApiModelProperty(value = "")
    private String telephone;

    @ApiModelProperty(value = "")
    private String email;

    @ApiModelProperty(value = "")
    private Long loginStatus;

    @ApiModelProperty(value = "")
    private Long status;

    @ApiModelProperty(value = "创建时间")
    private Long createDate;

    @ApiModelProperty(value = "修改时间")
    private Long modifyDate;

    @ApiModelProperty(value = "最后登录时间")
    private Long lastloginDate;

    @ApiModelProperty(value = "")
    private String signId;

    @ApiModelProperty(value = "")
    private String authIds;

    @ApiModelProperty(value = "")
    private Boolean departmentAdmin;

    @ApiModelProperty(value = "登录失败次数")
    private Integer loginFailureTimes;

    @ApiModelProperty(value = "重置密码时间")
    private Long resetPwdDate;

    @ApiModelProperty(value = "账户剩余冻结时间")
    private String frozenTimes;

    @ApiModelProperty(value = "账户冻结日期")
    private Long frozenDate;

    @ApiModelProperty(value = "租户命名空间")
    private String namespace;

}