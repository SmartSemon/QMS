package com.usc.app.query;

import java.util.List;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.query.search.i.AbstractSearchAction;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.md.ItemInfo;

public class QuerySingleItemData extends AbstractAction implements AbstractSearchAction
{

	@Override
	public Object executeAction() throws Exception
	{
		String condition = getCondition(context);
		ItemInfo itemInfo = context.getItemInfo();

		List dataList = DBUtil.getSQLResultByConditionLimit(itemInfo, condition, getDataPage(context));
		return queryTrue(dataList);
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
