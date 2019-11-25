package com.usc.app.action;

import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;

public class CreateTreeObjAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		if (disable())
		{
			if (!context.getItemInfo().containsField("pid"))
			{
				return failedOperation();
			}
			Map<String, Object> newData = ActionParamParser.getFieldVaues(context.getItemInfo(), context.getItemPage(),
					context.getInitData());
			String pid = (String) context.getExtendInfo("pid");
			newData.put("pid", pid);
			context.setInitData(newData);
			CreateObjAction createObjAction = new CreateObjAction();
			createObjAction.setApplicationContext(context);
			return createObjAction.action();
		}
		return StandardResultTranslate.getResult(true, "Action_Create");
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
