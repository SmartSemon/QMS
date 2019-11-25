package com.usc.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;

/**
 * @description 常用辅助工具类
 * @author caochengde
 * @date 2018年12月11日
 */
public class Utils
{

	/**
	 * @description 判断对象是否为Empty，为空返回true
	 * @author caochengde
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj)
	{
		if (obj == null)
		{
			return true;
		}
		if (obj == "")
		{
			return true;
		}
		if (obj instanceof String)
		{
			if (((String) obj).length() == 0)
			{
				return true;
			}
		}
		if (obj instanceof Collection)
		{
			if (((Collection<?>) obj).size() == 0)
			{
				return true;
			}
		}
		if (obj instanceof Map)
		{
			if (((Map<?, ?>) obj).size() == 0)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @description 将java.util.Date对象格式化为："yyyy-MM-dd HH:mm:ss"
	 * @author caochengde
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
		if (date != null)
		{
			return df.format(date);
		}
		return null;
	}

	/**
	 * @description 将"yyyy-MM-dd HH:mm:ss"转换为java.util.Date对象
	 * @author caochengde
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String str)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try
		{
			date = df.parse(str);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @description 将"yyyy-MM-dd HH:mm:ss"转换为java.sql.Date对象，并格式化
	 * @author caochengde
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static String stringToSqlDate(String str)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = null;
		try
		{
			Date date1 = df.parse(str);
			java.sql.Date date = new java.sql.Date(date1.getTime());
			dateStr = df.format(date);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * @description 将java.util.Date转换为java.sql.Date对象，并格式化
	 * @author caochengde
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String dateToSqlDate(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		String dateStr = df.format(sqlDate);
		return dateStr;
	}

	/**
	 * @description 将数组中带下划线的key转换为驼峰命名
	 * @author caochengde
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> listUnderscoreToCamelCase(List<T> list)
	{
		if (list.size() > 0)
		{
			List<T> resultList = new ArrayList<T>();
			for (Dto dto : (List<Dto>) list)
			{
				resultList.add(dtoUnderscoreToCamelCase(dto));
			}
			return (List<T>) resultList;
		}
		return (List<T>) list;
	}

	/**
	 * @description 将Dto中带下划线的key转换为驼峰命名
	 * @author caochengde
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dtoUnderscoreToCamelCase(Dto dto)
	{
		if (dto != null)
		{
			Dto newDto = new MapDto();
			Iterator<Dto.Entry<String, Object>> it = dto.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<String, Object> entry = it.next();
				if (entry.getValue() instanceof List)
				{
					List<T> value = listUnderscoreToCamelCase((List<T>) entry.getValue());
					String key = entry.getKey();
					String newKey = underscoreToCamelCase(key);
					newDto.put(newKey, value);
					continue;
				}
				if (entry.getValue() instanceof Dto)
				{
					Dto value = dtoUnderscoreToCamelCase((Dto) entry.getValue());
					String key = entry.getKey();
					String newKey = underscoreToCamelCase(key);
					newDto.put(newKey, value);
					continue;
				}
				String key = entry.getKey();
				String newKey = underscoreToCamelCase(key);
				newDto.put(newKey, entry.getValue());
			}
			return (T) newDto;
		}
		return (T) dto;
	}

	/**
	 * @description 将带下划线的String转换为驼峰命名
	 * @author caochengde
	 * @param str
	 * @return
	 */
	public static String underscoreToCamelCase(String str)
	{
		StringBuilder sb = new StringBuilder();
		String[] strArray = str.split("_");
		sb.append(strArray[0]);
		for (int i = 1; i < strArray.length; i++)
		{
			sb.append(strArray[i].substring(0, 1).toUpperCase());
			sb.append(strArray[i].substring(1));
		}
		return sb.toString();
	}

	/**
	 * @description 将数组中驼峰命名的key转换为带下划线
	 * @author caochengde
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> listCamelCaseToUnderscore(List<T> list)
	{
		if (list.size() > 0)
		{
			List<T> resultList = new ArrayList<T>();
			for (Dto dto : (List<Dto>) list)
			{
				resultList.add(dtoCamelCaseToUnderscore(dto));
			}
			return (List<T>) resultList;
		}
		return (List<T>) list;
	}

	/**
	 * @description 将Dto中驼峰命名的key转换为带下划线
	 * @author caochengde
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dtoCamelCaseToUnderscore(Dto dto)
	{
		if (dto != null)
		{
			Dto newDto = new MapDto();
			Iterator<Dto.Entry<String, Object>> it = dto.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<String, Object> entry = it.next();
				if (entry.getValue() instanceof List)
				{
					List<T> value = listUnderscoreToCamelCase((List<T>) entry.getValue());
					String key = entry.getKey();
					String newKey = CamelCaseToUnderscore(key);
					newDto.put(newKey, value);
					continue;
				}
				if (entry.getValue() instanceof Dto)
				{
					Dto value = dtoCamelCaseToUnderscore((Dto) entry.getValue());
					String key = entry.getKey();
					String newKey = CamelCaseToUnderscore(key);
					newDto.put(newKey, value);
					continue;
				}
				String key = entry.getKey();
				String newKey = CamelCaseToUnderscore(key);
				newDto.put(newKey, entry.getValue());
			}
			return (T) newDto;
		}
		return (T) dto;
	}

	/**
	 * @description 将驼峰命名的String转换为带下划线
	 * @author caochengde
	 * @param dto
	 * @return
	 */
	public static String CamelCaseToUnderscore(String str)
	{
		StringBuilder sb = new StringBuilder();
		char[] arr = str.toCharArray();
		for (int i = 0; i < arr.length; i++)
		{
			char c = arr[i];
			if (c >= 'A' && c <= 'Z')
			{
				c += 32; // 大写转换小写+32，反之-32
				sb.append("_" + c);
			} else
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 毫秒转化为时分秒
	 *
	 * @param ms
	 * @return
	 */
	public static String msToHms(Long ms)
	{
		if (ms != null)
		{
			// 初始化Formatter的转换格式。
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			// 设置时区
			formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
			return formatter.format(ms);
		}
		return null;
	}

}
