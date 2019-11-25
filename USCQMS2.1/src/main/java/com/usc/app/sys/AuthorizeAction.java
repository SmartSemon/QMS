package com.usc.app.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.usc.app.action.a.AbstractAction;
import com.usc.app.query.QueryFunctionalPermissionsData;
import com.usc.obj.api.USCObject;
import com.usc.server.jdbc.SDBUtils;
import com.usc.util.ObjectHelperUtils;

public class AuthorizeAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject object = context.getSelectedObj();
		List<String> oldList = new ArrayList<String>();
		Object hAuthArrayString = context.getExtendInfo("hAuthArray");
		List<String> hIDS = (hAuthArrayString == null || "null".equals(hAuthArrayString.toString())
				? new ArrayList<String>()
				: JSONArray.parseArray((String) hAuthArrayString, String.class));
		oldList.addAll(hIDS);
		Object hHalfAuthArray = context.getExtendInfo("hHalfAuthArray");
		List<String> hHIDS = (hHalfAuthArray == null || "null".equals(hHalfAuthArray.toString()) ? null
				: JSONArray.parseArray((String) hHalfAuthArray, String.class));
		oldList.addAll(hHIDS);

		Object nAuthArrayString = context.getExtendInfo("nAuthArray");
		List<Map> nAuthData = (nAuthArrayString == null || "null".equals(nAuthArrayString.toString()) ? null
				: JSONArray.parseArray(nAuthArrayString.toString(), Map.class));

		if (ObjectHelperUtils.isEmpty(oldList) && ObjectHelperUtils.isEmpty(nAuthData))
		{
			return failedOperation();
		}
		if (ObjectHelperUtils.isEmpty(oldList) && !ObjectHelperUtils.isEmpty(nAuthData))
		{
			SDBUtils.authorize(object, nAuthData, context);
		}
		if (!ObjectHelperUtils.isEmpty(oldList) && ObjectHelperUtils.isEmpty(nAuthData))
		{
			SDBUtils.cancelAuthorize(object, oldList, context);
		}
		if (!ObjectHelperUtils.isEmpty(oldList) && !ObjectHelperUtils.isEmpty(nAuthData))
		{
			SDBUtils.updateAuthorize(object, hIDS, hHIDS, nAuthData, context);
		}
		QueryFunctionalPermissionsData functionalPermissionsData = new QueryFunctionalPermissionsData();
		functionalPermissionsData.setApplicationContext(context);
		return functionalPermissionsData.action();
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
