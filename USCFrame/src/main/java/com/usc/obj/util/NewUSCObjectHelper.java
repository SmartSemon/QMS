package com.usc.obj.util;

import java.util.Map;

import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ObjectCachingDataHelper;
import com.usc.server.md.ItemInfo;
import com.usc.server.util.MateFactory;
import com.usc.util.ObjectHelperUtils;

public class NewUSCObjectHelper
{
	public static USCObject newObject(String itemNo, Map<String, Object> data)
	{
		if (ObjectHelperUtils.isEmpty(itemNo))
		{
			return null;
		}
		ItemInfo info = MateFactory.getItemInfo(itemNo);
		String impl = info.getImplClass();
		return (USCObject) ObjectCachingDataHelper.newInstanceParam(impl, itemNo, (Map<String, Object>) data);
	}
}
