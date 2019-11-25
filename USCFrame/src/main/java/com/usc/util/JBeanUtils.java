package com.usc.util;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.ArrayUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JBeanUtils
{
	public final static String PORT = ".";
	public final static String SLASH = "/";
	public final static String BACK_SLASH = "\\";

	public static Class<?> forName(String clazz)
	{
		try
		{
			return Class.forName(clazz);
		} catch (ClassNotFoundException e)
		{
			log.error(clazz + " >>> Class Not Found Exception", e);
		}
		return null;
	}

	public static Object newInstance(String clazz, Object... paramTypes)
	{

		if (isClassName(clazz))
		{
			try
			{
				Class<?> clas = forName(clazz);
				if (clas == null)
				{
					return null;
				}
				return newInstance(clas, paramTypes);
			} catch (Exception e)
			{
				log.error(clazz + " Class newInstance Exception", e);
			}
		}
		return null;
	}

	public static Object newInstance(Class<?> clazz, Object... paramTypes) throws Exception
	{
		Class<?>[] parameterTypes = null;
		if (!ArrayUtils.isEmpty(paramTypes))
		{
			int l = paramTypes.length;
			parameterTypes = new Class<?>[l];
			for (int i = 0; i < l; i++)
			{
				parameterTypes[i] = paramTypes[i].getClass();
			}
		}

		return newInstance(clazz.getConstructor(parameterTypes), paramTypes);
	}

	public static Object newInstance(Constructor<?> constructor, Object... paramTypes) throws Exception
	{
		return constructor.newInstance(paramTypes);
	}

	public static boolean isClassName(String clazz)
	{
		if (ObjectHelperUtils.isNotEmpty(clazz))
		{
			clazz = clazz.replace(SLASH, PORT).replace(BACK_SLASH, PORT);
			if (clazz.contains(PORT) && !clazz.startsWith(PORT) && clazz.endsWith(PORT))
			{
				return true;
			}
		}
		return false;
	}
}
