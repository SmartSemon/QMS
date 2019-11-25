package com.usc.app.action.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.usc.obj.api.USCObject;

public class OperationalCach
{
	public OperationalCach()
	{
	}

	private static ConcurrentHashMap<String, USCObject> mObjs = new ConcurrentHashMap<String, USCObject>();
	private static ConcurrentHashMap<String, USCObject> dObjs = new ConcurrentHashMap<String, USCObject>();

	public static USCObject getMData(String object)
	{
		if (isMData(object))
		{
			return mObjs.get(object);
		}
		return null;
	}

	public static USCObject getMData(USCObject object)
	{
		if (isMData(object))
		{
			return mObjs.get(object);
		}
		return null;
	}

	public static boolean isMData(Object object)
	{
		if (object instanceof USCObject)
		{
			return mObjs.containsKey(((USCObject) object).getID());
		}
		return mObjs.contains(object);

	}

	public static void putMObj(USCObject object)
	{
		if (!isDeleted(object))
		{
			mObjs.put(object.getID(), object);
		}
	}

	public static boolean isDeleted(USCObject object)
	{

		if (object instanceof USCObject)
		{
			return dObjs.containsKey(((USCObject) object).getID());
		}
		return dObjs.contains(object);
	}

	public static void putDeleteObj(USCObject object)
	{
		if (isDeleted(object))
		{
			return;
		}
		dObjs.put(object.getID(), object);
	}

}
