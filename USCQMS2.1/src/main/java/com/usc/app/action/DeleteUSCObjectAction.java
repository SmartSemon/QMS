package com.usc.app.action;

import com.usc.app.action.a.DUSCObjAction;
import com.usc.obj.api.USCObject;

public class DeleteUSCObjectAction extends DUSCObjAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject[] objects = context.getSelectObjs();
		for (USCObject uscObject : objects)
		{
			uscObject.delete(context);
		}
		return deleteSuccessful(null);
	}

	@Override
	public boolean disable() throws Exception
	{
		return false;
	}

}
