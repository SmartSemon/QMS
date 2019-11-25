package com.usc.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractClassAction;
import com.usc.obj.api.USCObject;

public class CreateClassNodeObjAction extends AbstractClassAction
{

	@Override
	public Object executeAction() throws Exception
	{
		String nodeID = nodeObj.getID();
		String nodeItemNo = nodeObj.getFieldValueToString("itemno");
		String itemNo = super.getClassNodeItemNo();
		Map data = ActionParamParser.getFieldVaues(context.getItemInfo(), super.getClassNodeItemProperty(),
				context.getInitData());
		data.put("pid", nodeID);
		data.put("itemno", nodeItemNo);
		context.setInitData(data);
		USCObject object = context.createObj(itemNo);
		List<Map> list = new ArrayList<Map>();
		list.add(object.getFieldValues());
		return createSuccessful(list);
	}

}
