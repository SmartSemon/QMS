package com.usc.server.md.field;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usc.server.md.ItemField;

/**
 * ClassName: FieldMappingConverter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: Field Mapping Converter(可选). 不建议继续使用<br/>
 * date: 2019年7月31日 下午4:36:07 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class FieldMappingConverter
{

	private final static String KEY = "key";
	private final static String NAME = "name";
	private final static String VALUES = "values";

	public static Object getKey2Value(ItemField field, Object object)
	{
		if (object == null)
			return null;
		String mapString = field.getEditParams();
		if (mapString == null)
		{
			return object;
		}
		Object key = null;
		JSONObject json = JSONObject.parseObject(mapString);
		List<Map> maps = JSONArray.parseArray(json.get(VALUES).toString(), Map.class);

		if (object instanceof String)
		{
			for (Map map : maps)
			{
				String name = map.get(KEY).toString();
				if (name.equals(String.valueOf(object)))
				{
					return (String) map.get(NAME);
				}
			}
		} else if (object instanceof Integer)
		{
			for (Map map : maps)
			{
				int name = Integer.valueOf(map.get(KEY).toString());
				if (name == Integer.valueOf((String.valueOf(object))))
				{
					return (String) map.get(NAME);
				}
			}
		}
		return object;

	}

	public static Object getValue2Key(ItemField field, Object object)
	{
		if (object == null)
			return null;
		String mapString = field.getEditParams();
		if (mapString == null)
		{
			return object;
		}
		Object key = null;
		JSONObject json = JSONObject.parseObject(mapString);
		List<Map> maps = JSONArray.parseArray(json.get(VALUES).toString(), Map.class);

		if (object instanceof String)
		{
			for (Map map : maps)
			{
				String name = map.get(NAME).toString();
				if (name.equals((String) (object)))
				{
					return getValue(map, KEY);
				}
			}
		} else if (object instanceof Integer)
		{
			for (Map map : maps)
			{
				int name = Integer.valueOf((String) map.get(NAME));
				if (name == Integer.valueOf((String) (object)))
				{
					return getValue(map, KEY);
				}
			}
		}
		return object;
	}

	public static String getValue(Map map, String key)
	{
		if (map != null)
		{
			return (String) map.get(key);
		}
		return null;
	}
}
