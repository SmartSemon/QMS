package com.usc.app.query;

import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.SearchUtils;
import com.usc.server.jdbc.DBUtil;

public class QueryAddRelationObjectData extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		if (context.getItemInfo() == null)
		{
			return failedOperation();
		}

		List<Map> rData = (List<Map>) context.getExtendInfo("rData");
		Object[] objects = null;
		int[] types = null;
		Map<String, Object> map = SearchUtils.getAddRelationPageDataCondition(rData);
		String condition = "del=0";

		if (map != null)
		{
			condition = condition + " AND " + map.get("condition");
			objects = (Object[]) map.get("objects");
			types = (int[]) map.get("types");
		}
		Object page = context.getExtendInfo("page");
		List dataList = DBUtil.getSQLResultByConditionLimit(context.getItemInfo(), condition, objects, types,
				page == null ? 1 : (int) page);
		return queryTrue(dataList);
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
