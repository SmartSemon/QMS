package com.usc.app.action;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;

public class AddRelationShipObjAction extends AbstractRelationAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject[] objects = context.getSelectObjs();
		if (objects == null)
		{
			return successfulOperation();
		}
		List dataList = new Vector<Map>(objects.length);
		for (USCObject uscObject : objects)
		{
			Map<String, Object> relMap = GetRelationData.getData(root, uscObject);
			ApplicationContext applicationContext = (ApplicationContext) context.cloneContext();
			applicationContext.setInitData(relMap);
			applicationContext.setItemNo(getRelationShip().getRelationItem());
			USCObject relObj = applicationContext.createObj(applicationContext.getItemNo());
			if (relObj != null)
			{
				dataList.add(uscObject.getFieldValues());
			}
		}
		return addSuccessful(dataList);
	}

}
