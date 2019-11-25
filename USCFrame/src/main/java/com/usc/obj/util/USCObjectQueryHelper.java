package com.usc.obj.util;

import java.util.List;
import java.util.Map;

import com.usc.app.execption.ThrowRemoteException;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.USCServerBeanProvider;
import com.usc.util.ObjectHelperUtils;

public class USCObjectQueryHelper
{
	public static USCObject getObjectByID(String itemNo, String id)
	{
		if (ObjectHelperUtils.isEmpty(itemNo) || ObjectHelperUtils.isEmpty(id))
		{
			return null;
		}
		try
		{
			Map<String, Object> data = USCServerBeanProvider.getItemBean().getRequestItemByID(itemNo, id);
			if (!ObjectHelperUtils.isEmpty(data))
			{
				return NewUSCObjectHelper.newObject(itemNo, data);
			}
		} catch (Exception e)
		{
			ThrowRemoteException.throwException(e);
		}
		return null;

	}

	public static USCObject getObjectByCondition(String itemNo, String condition)
	{
		USCObject[] objects = getObjectsByCondition(itemNo, condition);
		return ObjectHelperUtils.isEmpty(objects) ? null : objects[0];
	}

	public static USCObject[] getObjectsByCondition(String itemNo, String condition)
	{
		if (ObjectHelperUtils.isEmpty(itemNo) || ObjectHelperUtils.isEmpty(condition))
		{
			return null;
		}
		try
		{
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> datas = USCServerBeanProvider.getItemBean().getObjectsByCondition(itemNo,
					condition);
			if (!ObjectHelperUtils.isEmpty(datas))
			{
//				List<USCObject> list = new ArrayList<USCObject>(datas.size());
//				datas.forEach(data -> {
//					USCObject object = NewUSCObjectHelper.newObject(itemNo, data);
//					list.add(object);
//				});
//				return list.toArray(new USCObject[list.size()]);

				USCObject[] objects = new USCObject[datas.size()];
				for (int i = 0; i < datas.size(); i++)
				{
					USCObject object = NewUSCObjectHelper.newObject(itemNo, datas.get(i));
					objects[i] = object;
				}
				return objects;
			}
		} catch (Exception e)
		{
			ThrowRemoteException.throwException(e);
		}
		return null;

	}
}
