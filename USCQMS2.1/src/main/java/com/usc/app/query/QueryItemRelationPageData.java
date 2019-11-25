package com.usc.app.query;

import java.util.List;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.md.USCModelMate;

public class QueryItemRelationPageData extends AbstractRelationAction
{

	@Override
	public Object executeAction() throws Exception
	{
		String itemA = root.getItemNo();
		String itemAID = root.getID();
		String itemB = context.getItemNo();
		String rel = USCModelMate.getItemInfo(getRelationShip().getRelationItem()).getTableName();
		List dataList = DBUtil.getRelationItemResult(itemA, USCModelMate.getItemInfo(itemB), rel, itemAID, 1);
		return this.queryTrue(dataList);
	}

}
