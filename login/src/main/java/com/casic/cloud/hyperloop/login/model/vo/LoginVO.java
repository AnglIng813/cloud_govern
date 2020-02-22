package com.casic.cloud.hyperloop.login.model.vo;

import com.casic.cloud.hyperloop.common.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@ApiModel
public class LoginVO extends BaseVO {

    @NotBlank
    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    @Pattern(regexp = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$)^.{8,}$")
    @ApiModelProperty(value = "密码(密码长度至少为8位，必须包含大小写字母/数字/符号任意两者组合)", required = true)
    private String password;

    @NotBlank
    @ApiModelProperty(value = "验证码", required = true)
	private String verifyCode;

}