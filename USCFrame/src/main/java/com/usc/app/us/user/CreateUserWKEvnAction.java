package com.usc.app.us.user;

import java.util.HashMap;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;

public class CreateUserWKEvnAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject user = context.getSelectedObj();
		boolean exist = user.getFieldValue("WKCONTEXT") == null ? false : ((Boolean) user.getFieldValue("WKCONTEXT"));
		if (exist)
		{
			return StandardResultTranslate.getResult("用户：<  " + user.getFieldValueToString("SNAME") + " >已存在工作环境",
					new HashMap());
		}
		String userID = user.getID();
		String userName = user.getFieldValueToString("SNAME");
		boolean userState = user.getFieldValue("OPSTATE") == null ? false : ((Boolean) user.getFieldValue("OPSTATE"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SUID", userID);
		map.put("SUNAME", userName);
		map.put("SUSTATE", userState);

		ApplicationContext applicationContext = (ApplicationContext) context.cloneContext();
		applicationContext.setInitData(map);
		applicationContext.setItemNo("wkclient");
		USCObject object = applicationContext.createObj(applicationContext.getItemNo());
		if (object != null)
		{
			user.setFieldValue("WKCONTEXT", true);
			user.save(context);
			return successfulOperation();
		}
		return failedOperation();

	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
