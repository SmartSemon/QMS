package com.usc.obj.api.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;

import com.usc.app.action.AppActionFactory;
import com.usc.util.JBeanUtils;
import com.usc.util.ObjectHelperUtils;

public class ObjectCachingDataHelper
{
	private static ConcurrentHashMap<String, Class<?>> objClassData = new ConcurrentHashMap<String, Class<?>>();
	private static ConcurrentHashMap<String, Class<?>> objActionClassData = new ConcurrentHashMap<String, Class<?>>();

	public static Object getObjClassData(String object)
	{
		if (object == null)
		{
			return null;
		}

		if (objClassData.containsKey(object))
		{
			return objClassData.get(object);
		} else
		{
			Class<?> clas = AppActionFactory.getObjTypeClass(object);
			if (clas == null)
			{
				return null;
			}
			setObjClassData(object, clas);
		}
		return objClassData.get(object);
	}

	public static void setObjClassData(String obj, Class<?> class1)
	{
		if (ObjectHelperUtils.isEmpty(objClassData))
		{
			objClassData = new ConcurrentHashMap<>();
		}
		objClassData.put(obj, class1);
	}

	public static Object getObjActionClassData(String object)
	{
		if (object == null)
		{
			return null;
		}
		if (objActionClassData.containsKey(object))
		{
			return objActionClassData.get(object);
		}
		Class<?> clas = AppActionFactory.getObjTypeClass(object);
		if (clas == null)
		{
			return null;
		}
		setObjActionClassData(object, clas);
		return objActionClassData.get(object);
	}

	public static void setObjActionClassData(String obj, Class<?> class1)
	{
		if (ObjectHelperUtils.isEmpty(objActionClassData))
		{
			objActionClassData = new ConcurrentHashMap<>();
		}
		objActionClassData.put(obj, class1);
	}

	public static Object newInstance(String impl)
	{
		if (impl != null)
		{
			try
			{
				Class<?> clas = (Class<?>) getObjActionClassData(impl);
				return clas == null ? null : JBeanUtils.newInstance(clas.getConstructor());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Object newInstanceParam(String impl, Object... paramTypes)
	{
		if (impl != null)
		{
			try
			{
				Class<?> clas = (Class<?>) getObjClassData(impl);
				if (clas == null)
				{
					return null;
				}
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
				Constructor<?> constructor = clas.getConstructor(parameterTypes);
				return constructor.newInstance(paramTypes);
			} catch (InstantiationException e)
			{
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
				return null;
			} catch (IllegalArgumentException e)
			{
				e.printStackTrace();
				return null;
			} catch (InvocationTargetException e)
			{
				e.printStackTrace();
				return null;
			} catch (NoSuchMethodException e)
			{
				e.printStackTrace();
				return null;
			} catch (SecurityException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

}
