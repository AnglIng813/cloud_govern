package com.casic.cloud.hyperloop.common.utils;


import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.commons.lang.ArrayUtils;

@Slf4j
public class ConvertBeanUtils {


    /***
     * @Description: Map-->Bean
     * @Author: LDC
     * @Date: 2018/7/4 20:17
     * @version: V1.0
    */
    public static<T> void converMap2Bean(Map<String, Object> map,Object obj) {

        if(map.isEmpty()){
            return;
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String propertyName = property.getName();
                //获取字段类型
                String type = property.getPropertyType().getSimpleName();
                if (map.containsKey(propertyName )) {
                    Object value = map.get(propertyName);

                    if(value==null){
                        continue;
                    }

                    /**
                     * 单独处理类型
                     * 很多时候bean的成员变量不都是String类型的
                     * 可能会是很多类型。
                     */
                    if(StringUtils.equals("Date",type)){
                        if(value instanceof Long) {
                            Date date = new Date();
                            date.setTime((Long) value);
                            value = date;
                        }else if(value instanceof String){
                            value = DateUtil.convert2Date(DateUtil.String2LocalDateTime(String.valueOf(value)));
                        }
                    }else if(StringUtils.equals("Long",type)){
                        value = Long.parseLong(String.valueOf(value));
                    }else if(StringUtils.equals("Boolean",type)){
                        value = Boolean.parseBoolean(String.valueOf(value));
                    }else if(StringUtils.equals("Integer",type)){
                        value = Integer.parseInt(String.valueOf(value));
                    }else if(StringUtils.equals("Double",type)){
                        value = Double.parseDouble(String.valueOf(value));
                    }else if(StringUtils.equals("Float",type)){
                        value = Float.parseFloat(String.valueOf(value));
                    }else if(StringUtils.equals("BigDecimal",type)){
                        value = new BigDecimal(String.valueOf(value));
                    }//可继续追加类型



                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }
            }
        } catch (Exception e) {
            log.info("############ map-->bean转化异常 ##########");
            e.printStackTrace();
        }
    }

    /**
     * @Description: 拿到反射父类私有属性
     * @Author: LDC
     * @Date:2018/7/31 10:34
     */
    private static Field getPrivateField(String name, Class clazz) {
        Field declaredField = null;
        try {
            declaredField = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException ex) {

            if (clazz.getSuperclass() == null) {
                return declaredField;
            } else {
                declaredField = getPrivateField(name, clazz.getSuperclass());
            }
        }
        return declaredField;
    }

    /***
     * @Description: List<Map>-->List<Bean>
     * @TODO:
     * @Author: LDC
     * @Date: 2018/7/4 20:17
     * @version: V1.0
    */
    public static<T> List<T> converMap2BeanInList(List<Map<String, Object>> mapList,final Class<T> clazz) {

        if(mapList.size()<1){
            return null;
        }

        List<T> objList = new ArrayList<T>();
        T instance = null;
        try {
            for (int i = 0; i < mapList.size(); i++) {
                instance = clazz.newInstance();
                ConvertBeanUtils.converMap2Bean(mapList.get(i),instance);

                objList.add(instance);
            }
        } catch (Exception e) {
            log.info("############ List<Map>-->List<Bean>转化异常 ##########");
            e.printStackTrace();
        }
        return objList;
    }

    /***
     * @Description: Bean-->Map
     * @TODO:
     * @Author: LDC
     * @Date: 2018/7/4 20:19
     * @version: V1.0
    */
    public static Map<String, Object> converBean2Map(Object obj,Map<String, Object> map) {

        if(obj == null){
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            log.info("############ Bean-->Map转化常 ##########");
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @Description:  Bean-->Bean
     * @param source 原对象
     * @param clazz 目标对象
     * @param ignoreProperties 排除要copy的属性
     * @TODO:
     * @Author: LDC
     * @Date:2018/7/5 12:39
     * @version: V1.0
     */
    public static <T> T converBean2Bean(Object source, final Class<T> clazz, String...ignoreProperties){

        if(source==null){
            return null;
        }

        T targetInstance = null;
        try {
            targetInstance = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ArrayUtils.isEmpty(ignoreProperties)) {
            BeanUtils.copyProperties(source, targetInstance);
        }else {
            BeanUtils.copyProperties(source, targetInstance, ignoreProperties);
        }
        return targetInstance;

    }

    /**
     * @Description: List<Bean>-->List<Bean>
     * @TODO:
     * @Author: LDC
     * @Date:2018/7/5 13:48
     * @version: V1.0
     */
    public static<T> List converBean2BeanInList(List<T> beanList, Class clazz) {

        if(beanList==null || beanList.size()<1){
            return null;
        }

        List<Object> objList = new ArrayList<Object>();
        Object instance = null;
        try {
            for (int i = 0; i < beanList.size(); i++) {
                instance = clazz.newInstance();
                BeanUtils.copyProperties(beanList.get(i), instance);
                objList.add(instance);
            }
        } catch (Exception e) {
            log.info("############ List<Bean>-->List<Bean>转化异常 ##########");
            e.printStackTrace();
        }
        return objList;
    }

    /**
     * @Description: Page<Bean>-->Page<Bean>
     * @TODO:
     * @Author: LDC
     * @Date:2018/7/6 9:58
     * @version: V1.0
     */
    public static<T> Page converBean2BeanInPage(Page<T> oldPage, Class clazz) {

        if(oldPage==null){
            return null;
        }

        Page<Object> newPage = new Page<Object>();
        //设置分页信息
        newPage.setPageNum(oldPage.getPageNum());//当前页
        newPage.setPageSize(oldPage.getPageSize());//每页条数
        newPage.setPages(oldPage.getPages());//总页数
        newPage.setTotal(oldPage.getTotal());//总条数

        //防止分页信息丢失
        if (oldPage.size()<1){
            return newPage;
        }

        Object instance = null;
        try {
            for (int i = 0; i < oldPage.size(); i++) {
                instance = clazz.newInstance();
                BeanUtils.copyProperties(oldPage.get(i), instance);
                newPage.add(instance);
            }
        } catch (Exception e) {
            log.info("############ Page<Bean>-->Page<Bean>转化异常 ##########");
            e.printStackTrace();
        }
        return newPage;
    }

    /**
     * @Description: Page<Map>-->Page<Bean>
     * @TODO:
     * @Author: LDC
     * @Date:2018/7/6 9:58
     * @version: V1.0
     */
    public static<T> Page<T> converMap2BeanInPage(Page<Map<String, Object>> oldPage, final Class<T> clazz) {

        if(oldPage==null){
            return null;
        }

        Page<T> newPage = new Page<T>();
        //设置分页信息
        newPage.setPageNum(oldPage.getPageNum());//当前页
        newPage.setPageSize(oldPage.getPageSize());//每页条数
        newPage.setPages(oldPage.getPages());//总页数
        newPage.setTotal(oldPage.getTotal());//总条数

        //防止分页信息丢失
        if (oldPage.size()<1){
            return newPage;
        }

        T instance = null;
        try {
            for (int i = 0; i < oldPage.size(); i++) {
                instance = clazz.newInstance();
                ConvertBeanUtils.converMap2Bean(oldPage.get(i),instance);

                newPage.add(instance);
            }
        } catch (Exception e) {
            log.info("############ Page<Map>-->Page<Bean>转化异常 ##########");
            e.printStackTrace();
        }
        return newPage;
    }

}
