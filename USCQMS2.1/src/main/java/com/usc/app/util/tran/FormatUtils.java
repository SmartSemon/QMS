package com.usc.app.util.tran;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class FormatUtils
{
	public FormatUtils()
	{
	}

	private static final String LOGIN_PATH = "res/format/system/login.properties";

	private static final String AUTHOR_PATH = "res/format/system/author.properties";

	private static final String SYSTEMLOG_PATH = "res/format/system/systemlog.properties";
	private static final String SYSTEMUSER_PATH = "res/format/system/user.properties";

	private static final String SYSTEMCODE_PATH = "res/format/system/code/system.properties";

	public static String getSystemcodePath()
	{
		return SYSTEMCODE_PATH;
	}

	public static String getSystemuserPath()
	{
		return SYSTEMUSER_PATH;
	}

	public static String getSystemlogPath()
	{
		return SYSTEMLOG_PATH;
	}

	public static String getAuthorPath()
	{
		return AUTHOR_PATH;
	}

	public static String getLoginPath()
	{
		return LOGIN_PATH;
	}

	private static HashMap<String, HashMap<String, String>> valueMap = new HashMap<String, HashMap<String, String>>();

	private static void cacheProperties(String propName)
	{
		try
		{
			Properties properties = PropertiesLoaderUtils.loadAllProperties(propName);
			if (properties != null)
			{
				HashMap<String, String> map = new HashMap<String, String>();
				Enumeration<Object> keys = properties.keys();
				while (keys.hasMoreElements())
				{
					String key = (String) keys.nextElement();
					map.put(key, properties.getProperty(key));
				}
				valueMap.put(propName, map);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String getValue(String key, String propName)
	{
		if (!valueMap.containsKey(propName))
		{
			cacheProperties(propName);
		}

		HashMap<String, String> map = valueMap.get(propName);
		if (map.containsKey(key))
		{
			return map.get(key);
		}
		return null;
	}
}
