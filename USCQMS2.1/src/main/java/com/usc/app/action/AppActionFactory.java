package com.usc.app.action;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.usc.app.action.i.AppAction;
import com.usc.server.util.LoggerFactory;
import com.usc.util.JBeanUtils;

public class AppActionFactory
{
	static AppActionFactory factory = new AppActionFactory();
	protected ConcurrentHashMap<String, Class<?>> mapClassNameToClass = new ConcurrentHashMap<String, Class<?>>();
	protected List<String> allNoExistsClassName = new Vector<String>();

	public AppActionFactory()
	{
	}

	public static void setAPPActionFactory(AppActionFactory paramAppActionFactory)
	{
		factory = paramAppActionFactory;
	}

	public static AppActionFactory getAPPActionFactory()
	{
		return factory;
	}

	public AppAction getAppAction(String paramString)
	{
		Class<?> localClass = findActionClass(paramString);
		if (localClass == null)
		{
			return null;
		}
		try
		{
			Object localObject = JBeanUtils.newInstance(localClass);
			if ((localObject instanceof AppAction))
			{
				return (AppAction) localObject;
			}
		} catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return null;
	}

	protected Class<?> loadActionClass(String paramString)
	{
		try
		{
			return Class.forName(paramString);
		} catch (Exception localException)
		{
			LoggerFactory.logError("command(" + paramString + ") undfined");
		}
		return null;
	}

	public final Class<?> findActionClass(String paramString)
	{
		String str = ActionParamParser.getClassName(paramString);
		if ((str == null) || (str.length() == 0))
		{
			return null;
		}
		Class<?> localClass = (Class<?>) this.mapClassNameToClass.get(str);
		if (localClass != null)
		{
			return localClass;
		}
		if (this.allNoExistsClassName.contains(str))
		{
			return null;
		}
		localClass = loadActionClass(str);
		if (localClass != null)
		{
			this.mapClassNameToClass.put(str, localClass);
		}
		if (localClass == null)
		{
			this.allNoExistsClassName.add(str);
		}
		return localClass;
	}

	public static AppAction getAction(String paramString)
	{
		AppActionFactory localAppActionFactory = getAPPActionFactory();
		return localAppActionFactory.getAppAction(paramString);
	}

	public static Class<?> getActionClass(String paramString)
	{
		AppActionFactory localAppActionFactory = getAPPActionFactory();
		return localAppActionFactory.findActionClass(paramString);
	}

	public static Class<?> getObjTypeClass(String paramString)
	{
		AppActionFactory localAppActionFactory = getAPPActionFactory();
		Class<?> clazz = localAppActionFactory.mapClassNameToClass.get(paramString);
		return clazz == null ? localAppActionFactory.findActionClass(paramString) : clazz;
	}
}
