package com.usc.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.usc.server.md.mapper.DataClassType;

/**
 *
 * <p>
 * Title: BeanFactoryConverter
 * </p>
 *
 * <p>
 * Description: JavaBeanApdter
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年4月26日
 */
public class BeanFactoryConverter {

	public static <T> T getBean(Class<T> calss, ResultSet resultSet) throws Exception {
		return createBean(calss, resultSet);
	}

	public static <T> List<T> getBeans(Class<T> calss, ResultSet resultSet) {
		List<T> ts = null;
		try {
			ts = new ArrayList<>();
			while (resultSet.next()) {
				ts.add(createBean(calss, resultSet));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}

	private static <T> T createBean(Class<T> calss, ResultSet resultSet)
			throws Exception {
		T object = calss.newInstance();
		Field[] fields = calss.getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();

			try {
				resultSet.findColumn(fieldName.toLowerCase());
			} catch (SQLException e) {
				continue;
			}

			Object fieldVlaue = resultSet.getObject(fieldName.toLowerCase());
			if (fieldVlaue != null) {
				doMethod(field, calss, object, fieldVlaue);
			}

		}
		return object;
	}

	public static <T> T getJsonBean(Class<T> calss,@NotNull Object json) throws Exception {
		if (json == null) {
			return null;
		}

		return createBean(calss,
				json instanceof String ? JSON.parseObject((String) json)
						: (JSONObject) json);
	}


	private static <T> T createBean(Class<T> calss, JSONObject jsonObject)
			throws Exception {
		T object = calss.newInstance();
		Map map = JSON.parseObject(jsonObject.toJSONString());
//		List<Field> fields = new ArrayList<Field>();
		Class class1 = calss;

		while (class1 != null) {
			Field[] fields = class1.getDeclaredFields();
			forEachFields(object,class1,map,fields);
//			fields.addAll(Arrays.asList(class1.getDeclaredFields()));
			class1 = class1.getSuperclass();
		}


		return object;
	}

	private static <T> T  forEachFields(T object,Class<T> calss,Map map,Field[] fields) throws Exception {
		if (fields == null || fields.length==0) {
			return null;
		}
		for (Field field : fields) {
			Object fieldVlaue = null;
			String fieldName = field.getName();
			for (Object key : map.keySet()) {
				String fKey = String.valueOf(key).toUpperCase();
				if (fKey.equals(fieldName.toUpperCase())) {
					fieldVlaue = map.get(key);
					if (fKey.endsWith("DATA") && isJson(fieldVlaue)) {
						Object object1 = JSON.parse(String.valueOf(fieldVlaue));
						if (object1 instanceof JSONObject) {
							fieldVlaue = JSONObject.parseObject(String.valueOf(fieldVlaue), Map.class);
						} else {
							fieldVlaue = JSONArray.parseArray(String.valueOf(fieldVlaue), Map.class);
						}
					}
					map.remove(key);
					break;
				}
			}

			if (fieldVlaue != null) {
				doMethod(field, calss, object, fieldVlaue);
			}
		}
		return object;

	}

	private static <T> void doMethod(Field field, Class<T> calss, T object,
			Object fieldVlaue) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		String fieldName = field.getName();
		String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		Class<?> type = field.getType();
		Method method = calss.getDeclaredMethod(setMethodName, type);
		if (field.getType().getSimpleName().equals("int") && fieldVlaue==null){

		}else {
			method.invoke(object, DataClassType.getValue(type, fieldVlaue));
		}

	}

	public static boolean isJson(Object content) {
		try {
			JSON.parse(String.valueOf(content));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
