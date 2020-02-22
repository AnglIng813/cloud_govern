package com.casic.cloud.hyperloop.common.model.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 返回体基类（controller层）
 * @TODO:
 * @Date: 2018/4/23 17:12
 * @version: V1.0
**/
@Data
@AllArgsConstructor
public abstract class BaseRes implements Serializable{
    private static final long serialVersionUID = 8222962294897050901L;
}
