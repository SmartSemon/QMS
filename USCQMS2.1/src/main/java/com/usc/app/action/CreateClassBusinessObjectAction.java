package com.usc.app.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractClassAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;

public class CreateClassBusinessObjectAction extends AbstractClassAction
{

	@Override
	public Object executeAction() throws Exception
	{
		Map map = ActionParamParser.getFieldVaues(context.getItemInfo(), context.getItemPage(), context.getInitData());
		context.setInitData(map);
		USCObject object = context.createObj(context.getItemNo());

		if (object != null)
		{
			String classItemNo = super.getClassItemInfo().getItemNo();
			Map<String, Object> clrData = new HashMap<String, Object>();
			clrData.put("nodeid", nodeObj.getID());
			clrData.put("itemid", object.getID());
			ApplicationContext applicationContext = (ApplicationContext) context.cloneContext();
			applicationContext.setItemNo(classItemNo);
			applicationContext.setInitData(clrData);
			USCObject crlObject = applicationContext.createObj(applicationContext.getItemNo());
			List<Map> list = new ArrayList<Map>();
			list.add(object.getFieldValues());
			return createSuccessful(list);
		} else
		{
			return context.getExtendInfo("CreateResult");
		}

	}

}
