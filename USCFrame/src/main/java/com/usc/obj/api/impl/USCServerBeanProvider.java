package com.usc.obj.api.impl;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.bean.ItemBean;
import com.usc.server.syslog.USCSLogger;

public class USCServerBeanProvider
{

	public static ItemBean getItemBean()
	{
		return new DefaultItemBeanImpl();
	}

	public static USCSLogger getSystemLogger()
	{
		return new USCSLogger();
	}

	public static InvokeContext getContext(String itemNo, String userName)
	{
		return new ApplicationContext(itemNo, userName);
	}

	public static InvokeContext getContext(String userName, USCObject... objs)
	{
		return new ApplicationContext(userName, objs);
	}

}
