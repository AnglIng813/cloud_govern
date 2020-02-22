package com.casic.cloud.hyperloop.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Comments : 反射工具类
 */
public class ReflectUtil {
	/**
	 * @description 设置对象属性值
	 * @param obj
	 *            实体对象
	 * @param fieldName
	 *            属性名
	 * @param value
	 *            属性值
	 */
	public static void setProperty(Object obj, String fieldName, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			if (field != null) {
				Class<?> fieldType = field.getType();
				field.setAccessible(true);
				// 根据字段类型给字段赋值
				if (String.class == fieldType) {
					field.set(obj, String.valueOf(value));
				} else if ((Integer.TYPE == fieldType)
						|| (Integer.class == fieldType)) {
					field.set(obj, Integer.parseInt(value.toString()));
				} else if ((Long.TYPE == fieldType)
						|| (Long.class == fieldType)) {
					field.set(obj, Long.valueOf(value.toString()));
				} else if ((Float.TYPE == fieldType)
						|| (Float.class == fieldType)) {
					field.set(obj, Float.valueOf(value.toString()));
				} else if ((Short.TYPE == fieldType)
						|| (Short.class == fieldType)) {
					field.set(obj, Short.valueOf(value.toString()));
				} else if ((Double.TYPE == fieldType)
						|| (Double.class == fieldType)) {
					field.set(obj, Double.valueOf(value.toString()));
				} else if (Character.TYPE == fieldType) {
					if ((value != null) && (value.toString().length() > 0)) {
						field.set(obj,
								Character.valueOf(value.toString().charAt(0)));
					}
				} else if (Date.class == fieldType) {
					if (value instanceof Date) {
						field.set(obj, value);
					} else if (value instanceof String) {
						field.set(obj, new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").parse(value.toString()));
					}
				} else {
					field.set(obj, value);
				}
				field.setAccessible(false);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	/**
	 * @description 获取对象属性值
	 * @param obj
	 *            实体对象
	 * @param fieldName
	 *            属性名

	 */
	public static Object getProperty(Object obj, String fieldName) {
		Field field = getFieldName(obj, fieldName);
		Object value = null;
		try {
			if (field != null) {
				Class<?> fieldType = field.getType();
				field.setAccessible(true);
				// 根据字段类型给字段赋值
				if (Date.class == fieldType) {
					Object o = field.get(obj);
					if (o != null) {
						value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(o);
					}
				} else {
					value = field.get(obj);
				}
				field.setAccessible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	// 获取department.name 属性值
	public static Object getNestedProperty(Object obj, String fieldName) {
		Object value = null;
		String[] attributes = fieldName.split("\\.");
		try {
			if (obj instanceof Map) {
				Map<String, Object> mapItem = (Map<String, Object>)obj;
				/*SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(fieldName.equals("createTime") || fieldName.equals("updateTime")){
					value = sdf1.format(mapItem.get(fieldName));
				}else {
					value = mapItem.get(fieldName);
				}*/
				value = mapItem.get(fieldName);
			} else {
				value = getProperty(obj, attributes[0]);
				for (int i = 1; i < attributes.length; i++) {
					value = getProperty(value, attributes[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	// 获取属性
	public static Field getFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 获取对象所有字段的名字
	 * @param obj 目标对象
	 * @return 字段名字的数组
	 */
	public static String[] getFieldNames(Object obj) {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			if ((fields[i].getModifiers() & Modifier.STATIC) == 0) {
				fieldNames.add(fields[i].getName());
			}
		}
		return fieldNames.toArray(new String[fieldNames.size()]);
	}
	public void say(String name){
		
	}
}

