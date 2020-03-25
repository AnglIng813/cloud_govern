package com.sinochem.b2b.hyperloop.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:二进制状态控制工具包
 * @TODO:
 * @Author: LDC
 * @Date: 2018/07/03 19:35
 * @version: V1.0
 */
public class BinaryStateControlUtils {
    /**
     * @Description: 判断是否选中
     * @TODO: daState（已有状态）state状态值（常量）
     * @Author: LDC
     * @Date:2018/7/3 19:35
     * @version: V1.0
     */
    public static boolean hasState(long dbState, long state) {
        return (dbState & state) != 0;
    }

    /**
     * @Description: 判断是否选中, 返回0表示否 1表示是
     * @TODO: daState（已有状态） state状态值（常量）
     * @Author: LDC
     * @Date:2018/7/3 19:35
     * @version: V1.0
     */
    public static String hasStateOver(String dbState, String state) {
        long lDbState = convertNumTo10(dbState);
        long lDelState = convertNumTo10(state);
        if ((lDbState & lDelState) != 0) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * @Description: 添加一个状态
     * @TODO: dbState 已有状态 newState 需要添加的状态
     * @Author: LDC
     * @Date:2018/7/3 19:37
     * @version: V1.0
     */
    public static long addState(long dbState, long newState) {
        //验证是否已经添加该状态,已添加直接返回
        if (hasState(dbState, newState)) {
            return dbState;
        }
        return (dbState | newState);
    }

    /**
     * @Description: 删除一个状态
     * @TODO:
     * @Author: LDC
     * @Date:2018/7/3 20:57
     * @version: V1.0
     */
    public static long delState(long dbState, long delState) {
        //验证是否已经删除该状态,已添加直接返回
        if (!hasState(dbState, delState)) {
            return dbState;
        }
        return (dbState ^ delState);
    }

    /**
     * @Description: 添加一个状态，并自动补全0 返回12位string码
     * @TODO: dbState 已有状态 newState 需要添加的状态
     * @Author: LDC
     * @Date:2018/7/3 19:37
     * @version: V1.0
     */
    public static String addStateString(String dbState, String newState) {
        //验证是否已经添加该状态,已添加直接返回
        long lDbState = convertNumTo10(dbState);
        long lDelState = convertNumTo10(newState);
        if (hasState(lDbState, lDelState)) {
            return dbState;
        }
        String str = convertNumTo2(lDbState | lDelState);
        return StringUtils.leftPad(str, 12, "0");
    }

    /**
     * @Description: 删除一个状态, 并自动补全0 返回12位string码
     * @TODO:
     * @Author: LDC
     * @Date:2018/7/3 20:57
     * @version: V1.0
     */
    public static String delStateString(String dbState, String delState) {
        //验证是否已经删除该状态,已添加直接返回
        long lDbState = convertNumTo10(dbState);
        long lDelState = convertNumTo10(delState);
        if (!hasState(lDbState, lDelState)) {
            return dbState;
        }
        String str = convertNumTo2(lDbState ^ lDelState);
        return StringUtils.leftPad(str, 12, "0");
    }

    /**
     * @Description: 判断站点是否选中
     * @TODO:
     * @Author: ZJZ
     * @Date:2018/7/11 12:57
     * @version: V1.0
     */
    public static String checkState(String dbState, int position) {
        if (StringUtils.isEmpty(dbState)) {
            return "0";
        }

        int size = position;
        //如果传入的值大于偏移量，补0至其长度
        if (dbState.length() > position) {
            size = dbState.length();
        }
        //注:先补0再计算
        dbState = StringUtils.leftPad(dbState, size, "0");

        int initValue = 1;
        Long state = convertNumTo10(dbState);
        initValue <<= (position - 1);
        return (initValue & state) != 0 ? "1" : "0";
    }

    /**
     * @Description: 设置状态
     * @TODO:
     * @Author: ZJZ
     * @Date:2018/7/11 12:57
     * @version: V1.0
     */
    public static String setStateString(String dbState, int position) {
        int size = position;
        //如果传入的值大于偏移量，补0至其长度
        if (StringUtils.isEmpty(dbState)) {
            dbState = "";
        }
        if (dbState.length() > position) {
            size = dbState.length();
        }
        //注:先补0再计算
        dbState = StringUtils.leftPad(dbState, size, "0");

        int initValue = 1;
        Long state = convertNumTo10(dbState);
        initValue <<= (position - 1);
        String str = convertNumTo2(state | initValue);
        return StringUtils.leftPad(str, size, "0");
    }

    /**
     * 删除状态
     * @param dbState
     * @param position 从1开始
     * @return
     */

    public static String delStateString(String dbState, int position) {
        int size = position == 0 ? 1 : position;
        //如果传入的值大于偏移量，补0至其长度
        if (StringUtils.isEmpty(dbState)) {
            dbState = "";
        }
        if (dbState.length() > position) {
            size = dbState.length();
        }
        //注:先补0再计算
        dbState = StringUtils.leftPad(dbState, size, "0");//默认状态

        //如果要删除的位已经是0，则不进行计算，直接返回
        // 注 position 从1开始
        if (dbState.charAt(dbState.length() - position) == '0') {
            return dbState;
        }

        int initValue = 1;
        Long state = convertNumTo10(dbState);
        initValue <<= (position - 1);
        String str = convertNumTo2(state ^ initValue);
        return StringUtils.leftPad(str, size, "0");
    }


    /**
     * @Description: 二进制转十进制
     * @TODO:
     * @Author: LDC
     * @Date:2018/7/3 21:21
     * @version: V1.0
     */
    public static long convertNumTo10(String str) {
        return Long.valueOf(str, 2);
    }

    /**
     * @Description: 十进制转二进制
     * @TODO:
     * @Author: LDC
     * @Date:2018/7/3 21:27
     * @version: V1.0
     */
    public static String convertNumTo2(long num) {
        return Long.toBinaryString(num);
    }

    public static void main(String[] args) {
        String str = "0011";

        str = delStateString(str, 4);
        System.out.println("删除值---->" + str);
    }
}
