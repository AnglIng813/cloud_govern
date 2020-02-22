package com.casic.cloud.hyperloop.common.utils;

import com.casic.cloud.hyperloop.common.enums.CodeEnum;
import org.apache.commons.lang3.StringUtils;

/***
 * @Description: Enum处理工具
 * @Author: LDC
 * @Date: 2020/1/2 15:43
 */
public class EnumUtil {

    /**
     * 根据value找到对应的enum
     *
     * @param value
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum> T getByValue(String value, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (StringUtils.equals(value,each.getValue())) {
                return each;
            }
        }

        return null;
    }

    /**
     * 根据value找到对应的enum
     *
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }

        return null;
    }

    /**
     * indexOf,传入的参数ordinal指的是需要的枚举值在设定的枚举类中的顺序，以0开始
     * T
     * @param enumClass
     * @param ordinal
     */
    public static <T extends Enum<T>> T indexOf(Class<T> enumClass, int ordinal){
        return (T) enumClass.getEnumConstants()[ordinal];
    }
}
