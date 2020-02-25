package com.casic.cloud.hyperloop.core.model.dto;

import com.casic.cloud.hyperloop.common.model.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: AnglIng
 * @Date: 2020/2/25 15:42
 * @version: V1.0
 */

@Data
public class UserDTO extends BaseDTO {

    @ApiModelProperty(value = "当前页", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "需要数据条数", example = "20")
    private Integer pageSize;


    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

}
