package com.usc.obj.util;

import org.springframework.util.Assert;

import com.usc.app.action.mate.MateFactory;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.impl.ObjectCachingDataHelper;
import com.usc.obj.api.impl.a.DefaultInvokeContext;

public class ApplicationCreator
{
	private static ApplicationCreator contextCreator = new ApplicationCreator();

	public static ApplicationCreator newContext()
	{
		return contextCreator;
	}

	public DefaultInvokeContext createSyatemConext(String userName, USCObject... objects)
	{
		Assert.notNull(objects);
		ApplicationContext context = new ApplicationContext(userName, objects);
		String itemNo = objects[0].getItemNo();
		context.setItemNo(itemNo);
		context.setSelectObjs(objects);
		context.setUserName(userName != null ? userName : "admin");
		USCObjectAction objectAction = (USCObjectAction) ObjectCachingDataHelper
				.newInstance(MateFactory.getItemInfo(itemNo).getImplClass() + "Action");
		context.setActionObjType(objectAction);

		return context;

	}
}
