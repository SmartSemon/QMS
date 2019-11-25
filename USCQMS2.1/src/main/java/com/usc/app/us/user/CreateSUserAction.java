package com.usc.app.us.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.app.action.ActionParamParser;
import com.usc.app.action.a.AbstractRelationAction;
import com.usc.app.util.UserInfoUtils;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.autho.MD5.MD5Util;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.server.DBConnecter;
import com.usc.server.init.InitUserInfo;

public class CreateSUserAction extends AbstractRelationAction
{

	@Override
	public Object executeAction() throws Exception
	{
		Map userData = ActionParamParser.getFieldVaues(context.getItemInfo(), context.getItemPage(),
				context.getInitData());
		userData.put("PASSWORD", MD5Util.getMD5Ciphertext(UserInfoUtils.getDefaultPassWord()));
		context.setInitData(userData);
		USCObject newObj = context.createObj(context.getItemNo());
		if (newObj != null)
		{
			Map relationData = getRelationData(getRoot(), newObj);

			ApplicationContext applicationContext = (ApplicationContext) context.cloneContext();
			applicationContext.setInitData(relationData);
			applicationContext.createObj(getRelationShip().getRelationItem());
			InitUserInfo.run(new JdbcTemplate(DBConnecter.getDataSource()));
			return StandardResultTranslate.getResult(Boolean.TRUE, "Action_Create");
		}
		return context.getExtendInfo("CreateResult");
	}

	private Map<String, Object> getRelationData(USCObject root, USCObject newObj)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itema", root.getItemNo());
		map.put("itemaid", root.getID());
		map.put("itemb", newObj.getItemNo());
		map.put("itembid", newObj.getID());
		return map;
	}

}
