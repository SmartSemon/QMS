package com.usc.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import com.usc.util.ObjectHelperUtils;

public class PropertyFileUtil
{
	public static Map<Object, Object> propertice(File file)
	{
		Properties properties = new Properties();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			properties.load(br);
			br.close();
			Map<Object, Object> map = properties;
			return map;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Map application = new HashMap();

	public static String getApplicationPropertyValue(String key)
	{
		if (application.containsKey(key))
		{
			return (String) application.get(key);
		}
		Map classMap;
		try
		{
			classMap = propertice(new ClassPathResource("config/application.properties").getFile());
			if (!ObjectHelperUtils.isEmpty(classMap))
			{
				application.putAll(classMap);
				if (application.containsKey(key))
				{
					return (String) application.get(key);
				}
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;

	}

}
