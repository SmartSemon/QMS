package com.usc.app.entry;

import com.usc.app.action.a.AbstractClassAction;
import com.usc.app.action.a.AbstractRelationAction;
import com.usc.app.execption.ThrowRemoteException;
import com.usc.app.view.i.USCView;
import com.usc.obj.jsonbean.JSONBean;

public class InspectionImplClass
{
	public static void init(Object clazz, JSONBean jsonBean) throws Exception
	{
		if (isRelationAction(clazz.getClass()))
		{
			if (jsonBean.getItemA() == null || jsonBean.getItemAData() == null)
			{
				String exMsg = "Relational menu parameters not returned correctly";
				throw ThrowRemoteException.throwException(new NullPointerException(exMsg));
//				return;
			}
			createRelationContext(clazz, jsonBean);
		}
		if (isClassAction(clazz.getClass()))
		{
			createClassContext(clazz, jsonBean);
		}

		if (isObjectViewAction(clazz.getClass()))
		{
			createViewContext(clazz, jsonBean);
		}
	}

	private static boolean isClassAction(Class<? extends Object> class2)
	{

		return AbstractClassAction.class.isAssignableFrom(class2);
	}

	private static void createClassContext(Object clazz, JSONBean jsonBean) throws Exception
	{
		((AbstractClassAction) clazz).init(jsonBean);
	}

	private static boolean isRelationAction(Class<? extends Object> class2)
	{

		return AbstractRelationAction.class.isAssignableFrom(class2);
	}

	private static void createRelationContext(Object clazz, JSONBean jsonBean)
	{
		((AbstractRelationAction) clazz).init(jsonBean);
	}

	private static void createViewContext(Object clazz, JSONBean jsonBean)
	{
		((USCView) clazz).initView(jsonBean);
	}

	private static boolean isObjectViewAction(Class<? extends Object> class2)
	{

		return USCView.class.isAssignableFrom(class2);
	}
}
