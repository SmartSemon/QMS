package com.usc.app.query;

import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.app.query.search.i.AbstractSearchAction;
import com.usc.obj.util.USCObjExpHelper;
import com.usc.server.jdbc.DBUtil;

public class QueryItemRelationQueryViewData extends AbstractRelationAction implements AbstractSearchAction
{

	@Override
	public Object executeAction() throws Exception
	{
		String itemB = context.getItemNo();
		String queryViewCondition = getRelationQueryView().getWcondition();
		String condition = USCObjExpHelper.parseObjValueInExpression(root, queryViewCondition);
		List<Map<String, Object>> list = DBUtil.getSQLResultByConditionLimit(itemB, condition, getDataPage(context));
		return this.queryTrue(list);
	}
}
