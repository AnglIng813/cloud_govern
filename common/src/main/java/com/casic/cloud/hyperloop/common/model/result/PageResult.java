package com.casic.cloud.hyperloop.common.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:udb
 * @TODO:
 * @Author: LDC
 * @Date: 2018/07/05 14:35
 * @version: V1.0
 */
@Data
@ApiModel(value="响应信息列表model")
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable{

    @ApiModelProperty(value = "当前页",required = true)
    private Integer pageNum;

    @ApiModelProperty(value = "需要数据条数",required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "总页数",required = true)
    private Integer pages;

    @ApiModelProperty(value = "总条数",required = true)
    private Long total;

    @ApiModelProperty(value = "")
    private List<T> datas;


}
