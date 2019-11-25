package com.usc.server.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.expression.ParseException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * <p>
 * Title: BeanConverter
 * </p>
 *
 * <p>
 * Description: 1:将JavaBean 转换成Map、JSONObject 2:将JSONObject 转换成Map
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年4月22日
 *
 */
public class BeanConverter
{
	/**
	 * 将javaBean转换成Map
	 *
	 * @param javaBean javaBean
	 * @return Map对象
	 */
	public static Map<String, Object> toMap(Object javaBean)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		Method[] methods = javaBean.getClass().getDeclaredMethods();

		for (Method method : methods)
		{
			try
			{
				if (method.getName().startsWith("get"))
				{
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);

					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? null : value);
				}
			} catch (Exception e)
			{
			}
		}

		return result;
	}

	/**
	 * 将json对象转换成Map
	 *
	 * @param jsonObject json对象
	 * @return Map对象
	 */
	public static Map<String, Object> toMap(JSONObject jsonObject)
	{
		Map<String, Object> result = jsonObject;
		return result;
	}

	public static List<Map<String, Object>> jsonArrayToListMap(JSONArray jsonArray)
	{
		if (jsonArray == null)
			return null;
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.size(); i++)
		{
			Map<String, Object> Map = jsonArray.getJSONObject(i);
			listMap.add(Map);
		}
		return listMap;
	}

	/**
	 * 将javaBean转换成JSONObject
	 *
	 * @param bean javaBean
	 * @return json对象
	 */
	public static JSONObject toJSON(Object bean)
	{
		return new JSONObject(toMap(bean));
	}

	/**
	 * 将map转换成Javabean
	 *
	 * @param javabean javaBean
	 * @param data     map数据
	 */
	public static Object toJavaBean(Object javabean, Map<String, Object> data)
	{
		Method[] methods = javabean.getClass().getDeclaredMethods();
		for (Method method : methods)
		{
			try
			{
				if (method.getName().startsWith("set"))
				{
					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					method.invoke(javabean, new Object[]
					{ data.get(field) });
				}
			} catch (Exception e)
			{
			}
		}

		return javabean;
	}

	/**
	 * 将javaBean转换成JSONObject
	 *
	 * @param bean javaBean
	 * @return json对象
	 * @throws ParseException json解析异常
	 */
	public static void toJavaBean(Object javabean, String jsonStr) throws ParseException
	{
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		Map<String, Object> datas = toMap(jsonObject);
		toJavaBean(javabean, datas);
	}
}
