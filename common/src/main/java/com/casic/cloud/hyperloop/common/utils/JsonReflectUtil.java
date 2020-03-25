package com.yun.system.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.yun.system.common.exception.CloudApiServerException;
import com.yun.system.common.model.result.ApiErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 反射工具类，包含解析json并反射赋值方法
 * @Author: AnglIng
 * @Date: 2020/3/6 16:11
 * @version: V1.0
 */
@Slf4j
public class JsonReflectUtil extends ReflectUtil {

    public static JSONObject jsonObject;//读取内容的jsonObject
    public static Map<String, Map<String, String[]>> defaultAllMap = new HashMap<>();//可自定义
    public static Map<String, String[]> defaultKeyChain = new HashMap<>();//可自定义

    private static void initKeyChain() {
        //gateway配置
        Map<String, String[]> gatewayKeyChain = new HashMap<>();
        gatewayKeyChain.put("port", new String[]{"server", "port"});//端口号
        gatewayKeyChain.put("exposure", new String[]{"management", "endpoints", "web", "exposure", "include"});//actutor端点规则
        gatewayKeyChain.put("applicationName", new String[]{"spring", "application", "name"});//应用名
        gatewayKeyChain.put("discoveryAddr", new String[]{"spring", "cloud", "nacos", "discovery", "server-addr"});//nacos注册中心地址
        gatewayKeyChain.put("discoveryEnabled", new String[]{"spring", "cloud", "gateway", "discovery", "locator", "enabled"});//是否开启路由
        gatewayKeyChain.put("ignoreCase", new String[]{"spring", "cloud", "gateway", "discovery", "locator", "lower" +
                "-case-service-id"});//配置忽略大小写
        gatewayKeyChain.put("routeList", new String[]{"spring", "cloud", "gateway", "routes"});//路由规则
        defaultAllMap.put("gateway", gatewayKeyChain);

        //zuul配置
        Map<String, String[]> zuulKeyChain = new HashMap<>();
        zuulKeyChain.put("port", new String[]{"server", "port"});//端口号
        zuulKeyChain.put("applicationName", new String[]{"spring", "application", "name"});//应用名
        zuulKeyChain.put("defaultZone", new String[]{"eureka", "client", "service-url", "defaultZone"});//eureka注册中心地址
        zuulKeyChain.put("ribbonEnabled", new String[]{"ribbon", "eureka", "enabled"});//是否开启负载均衡
        zuulKeyChain.put("routeMap", new String[]{"zuul", "routes"});//路由规则
        defaultAllMap.put("zuul", zuulKeyChain);
    }

    static {
        if (defaultAllMap.isEmpty()) {
            //初始化
            initKeyChain();
        }
    }

    /**
     * @param t         需要设置内容的对象
     * @param fieldNames 字段列表
     */
    public static <T> void explainJson2Obj(T t, List<String> fieldNames) {
        fieldNames.stream().forEach(fieldName -> {
            explainJson2Obj(t, fieldName);
        });


    }

    /**
     * @param t         需要设置内容的对象
     * @param fieldName 字段名
     */
    public static <T> void explainJson2Obj(T t, String fieldName) {
        try {
            if (defaultKeyChain.isEmpty()) {
                log.info("请先对defaultKeyChain进行初始化");
                return;
            }
            if (!defaultKeyChain.containsKey(fieldName)) {
                return;
            }
            //数据处理
            String[] chain = defaultKeyChain.get(fieldName);
            processasamble(t, fieldName, chain);
        } catch (Exception e) {
            log.error("【解析json并反射赋值】失败");
            e.printStackTrace();
            throw new CloudApiServerException(ApiErrorCode.parsing_yaml_failure);
        }
    }

    /**
     * @param t         需要设置内容的对象
     * @param keyChain  关键字链map 其中key为对应的fieldName
     * @param fieldName 字段名
     */
    public static <T> void explainJson2Obj(T t, Map<String, String[]> keyChain, String fieldName) {
        try {
            if (!keyChain.containsKey(fieldName)) {
                return;
            }
            //数据处理
            String[] chain = keyChain.get(fieldName);
            processasamble(t, fieldName, chain);
        } catch (Exception e) {
            log.error("【解析json并反射赋值】失败");
            e.printStackTrace();
            throw new CloudApiServerException(ApiErrorCode.parsing_yaml_failure);
        }
    }

    /**
     * @param t         需要设置内容的对象
     * @param keyChain  关键字链
     * @param fieldName 字段名
     */
    public static <T> void explainJson2Obj(T t, String[] keyChain, String fieldName) {
        try {
            processasamble(t, fieldName, keyChain);
        } catch (Exception e) {
            log.error("【解析json并反射赋值】失败");
            e.printStackTrace();
            throw new CloudApiServerException(ApiErrorCode.parsing_yaml_failure);
        }
    }

    private static <T> void processasamble(T t, String fieldName, String[] chain) {
        JSONObject object = processConfig(chain, jsonObject, 0);
        //拿到配置文件中对应的值
        Object value = object.get(chain[chain.length - 1]);
        //设置到对应属性中
        setProperty(t, fieldName, value);
    }

    /**
     * 递归- 返回需要配置的那个对象，chain倒数第二个配置项
     * 因最后一项就是要返回的对象
     *
     * @return
     */
    public static JSONObject processConfig(String[] arr) {
        if(jsonObject.isEmpty()){
            log.info("请先对jsonObject进行设置");
            return null;
        }
        return processConfig(arr, jsonObject, 0);
    }

    /**
     * 递归- 返回需要配置的那个对象，chain倒数第二个配置项
     * 因最后一项就是要返回的对象
     *
     * @param jsonObject
     * @param i
     * @return
     */
    public static JSONObject processConfig(String[] arr, JSONObject jsonObject, int i) {
        JSONObject obj = (JSONObject) jsonObject;
        if (jsonObject.containsKey(arr[i])) {
            if (i != (arr.length - 1)) {
                obj = processConfig(arr, (JSONObject) jsonObject.get(arr[i]), ++i);
            }
            return (JSONObject) obj;
        }
        return null;
    }

    /**
     * 首字母转大写
     */
    public static String firstWord2UpperCase(String str) {
        String firstStr = String.valueOf(str.charAt(0)).toUpperCase();
        String substring = str.substring(1, str.length());
        return firstStr + substring;
    }


    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String removeLine(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }

        if (str.indexOf("_") > 0) { //下划线转驼峰
            String[] split = str.split("_");
            String temp = "";
            for (int i = 1; i < split.length; i++) {
                temp += firstWord2UpperCase(split[i]);
            }
            str = split[0] + temp;
        }
        return str;
    }

    //可自定义
    public static void setJsonObject(JSONObject jsonObject) {
        JsonReflectUtil.jsonObject = jsonObject;
    }

    //可自定义
    public static void setDefaultAllMap(Map<String, Map<String, String[]>> allMap) {
        JsonReflectUtil.defaultAllMap = allMap;
    }

    //可自定义
    public static void setDefaultKeyChain(Map<String, String[]> keyChain) {
        JsonReflectUtil.defaultKeyChain = keyChain;
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }

    public static Map<String, Map<String, String[]>> getDefaultAllMap() {
        return defaultAllMap;
    }

    public static Map<String, String[]> getDefaultKeyChain() {
        return defaultKeyChain;
    }

    public static void main(String[] args) {
        String a = "abc_cbd_dba";
        System.out.println(removeLine(a));
        System.out.println(firstWord2UpperCase(a));
    }
}
