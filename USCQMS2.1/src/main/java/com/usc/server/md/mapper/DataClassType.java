package com.usc.server.md.mapper;

public class DataClassType
{
	public static <T> Object getValue(Class<T> clazz, Object object)
	{
		String claszzString = clazz.toString();
		if (claszzString.endsWith("String"))
		{
			return String.valueOf(object);
		} else if (claszzString.endsWith("int"))
		{
			return Integer.valueOf(String.valueOf(object));
		}
		return object;

	}
}
