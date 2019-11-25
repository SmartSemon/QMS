package com.usc.app.util.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import com.usc.cache.redis.RedisUtil;

/**
 *
 * <p>
 * Title: StandardResultTranslate
 * </p>
 *
 * <p>
 * Description: 返回值对照器
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年4月28日
 *
 */
public class StandardResultTranslate
{
	private static final String ACTIONTRANSLATEMSG = "ACTIONTRANSLATEMSG";

	public static final boolean TRUE = Boolean.TRUE;
	public static final boolean FALSE = Boolean.FALSE;

	public static String translate(String string)
	{
		if (string == null || string.trim().equals(""))
		{
			return "";
		}
		RedisUtil redis = new RedisUtil();
		Map<Object, Object> map = (Map<Object, Object>) redis.get(ACTIONTRANSLATEMSG);
		if (map == null)
			return string;
		Object object = map.get(string);
		return object == null ? string : java.lang.String.valueOf(object);

	}

	public static Map<String, Object> getResult(Boolean flag, String actionString)
	{
		Map<String, Object> result = new HashMap<String, Object>();

		if (flag)
		{
			result.put("info", StandardResultTranslate.translate(actionString + "_1"));
		} else
		{
			result.put("info", StandardResultTranslate.translate(actionString + "_2"));
		}
		result.put("flag", flag);
		return result;
	}

	public static Map<String, Object> getResult(String actionString, List dataList)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", true);
		result.put("info", translate(actionString));
		if (dataList != null)
		{
			result.put("dataList", dataList);
		} else
		{

			result.put("dataList", new Vector());
		}
		return result;
	}

	public static Map<String, Object> getResult(String actionString, Map... dataList)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", true);
		result.put("info", translate(actionString));
		if (dataList != null)
		{
			result.put("dataList", dataList);
		} else
		{

			result.put("dataList", new Vector());
		}
		return result;
	}

	public static Map<String, Object> getQueryResult(Boolean flag, String actionString, List dataList)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		if (dataList != null)
		{
			result.put("dataList", dataList);
		} else
		{

			result.put("dataList", new Stack<>());
		}

		Map map = getResult(flag, actionString);
		result.putAll(map);
		return result;
	}

	public static Map<String, Object> getResult(String actionString, Boolean flag)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("info", actionString);
		return result;
	}

	public static Map<String, Object> successfulOperation()
	{
		Map<String, Object> result = getResult(true, "Action_Default");
		result.put("dataList", new Stack<>());
		return result;
	}

	public static Map<String, Object> failedOperation()
	{
		Map<String, Object> result = getResult(false, "Action_Default");
		result.put("dataList", new Stack<>());
		return result;
	}

	public static Map<String, Object> failedOperation(String mes)
	{
		Map<String, Object> result = getResult(mes, false);
		result.put("dataList", new Stack<>());
		return result;
	}

}
