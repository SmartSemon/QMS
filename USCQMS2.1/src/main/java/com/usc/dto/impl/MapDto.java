package com.usc.dto.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.usc.dto.Dto;

/**
 * ClassName: MapDto <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: Dto实现类. <br/>
 * date: 2019年7月31日 下午4:28:19 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class MapDto extends LinkedHashMap<String, Object> implements Dto, Serializable
{

	private static final long serialVersionUID = -2335753461744409680L;

	// c 无参构造
	public MapDto()
	{

	}

	// c 将Map转换为Dto
	public MapDto(Map<String, Object> map)
	{
		super(map);
	}

	// c 实例一个Dto对象
	public MapDto(String key, Object value)
	{
		super.put(key, value);
	}

	/**
	 * @describe 以Object类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public Object getObject(String key)
	{
		return super.get(key);
	}

	/**
	 * @describe 以String类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "String", null);
		if (obj != null)
		{
			return (String) obj;
		}
		return null;
	}

	public Integer getInteger(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "Integer", null);
		if (obj != null)
		{
			return (Integer) obj;
		}
		return null;
	}

	public Long getLong(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "Long", null);
		if (obj != null)
		{
			return (Long) obj;
		}
		return null;
	}

	public Double getDouble(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "Double", null);
		if (obj != null)
		{
			return (Double) obj;
		}
		return null;
	}

	public Float getFloat(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "Float", null);
		if (obj != null)
		{
			return (Float) obj;
		}
		return null;
	}

	public BigDecimal getBigDecimal(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "BigDecimal", null);
		if (obj != null)
		{
			return (BigDecimal) obj;
		}
		return null;
	}

	public Boolean getBoolean(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "Boolean", null);
		if (obj != null)
		{
			return (Boolean) obj;
		}
		return null;
	}

	public Date getDate(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "Date", "yyyy/MM/dd");
		if (obj != null)
		{
			return (Date) obj;
		}
		return null;
	}

	public Date getDateTime(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "Date", "yyyy-MM-dd hh:mm:ss");
		if (obj != null)
		{
			return (Date) obj;
		}
		return null;
	}

	public Timestamp getTimestamp(String key)
	{
		Object obj = ObjectConversionHelper.convert(get(key), "Timestamp", "yyyy-MM-dd hh:mm:ss");
		if (obj != null)
		{
			return (Timestamp) obj;
		}
		return null;
	}

	public void setDefaultAList(List<?> list)
	{
		put("defaultAList", list);
	}

	public void setDefaultBList(List<?> list)
	{
		put("defaultBList", list);
	}

	public <T> List<T> getDefaultAList()
	{
		return (List<T>) get("defaultAList");
	}

	public <T> List<T> getDefaultBList()
	{
		return (List<T>) get("defaultBList");
	}

	public void setDefaultJson(String jsonString)
	{
		put("defaultJsonString", jsonString);
	}

	public String getDefaultJson()
	{
		return getString("defaultJsonString");
	}

	public String toXml()
	{
		return null;
	}

	public String toXml(String Style)
	{
		return null;
	}

	public String toJson()
	{
		return null;
	}

	public String toJson(String format)
	{
		return null;
	}

	public String getKeySetToString()
	{
		String keys = "";
		for (String key : this.keySet())
		{
			keys += key + ",";
		}
		return keys.substring(0, keys.length() - 1);
	}

}
