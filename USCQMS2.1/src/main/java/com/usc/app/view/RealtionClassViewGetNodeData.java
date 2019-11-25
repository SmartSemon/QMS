package com.usc.app.view;

import java.util.List;

import com.usc.app.view.a.AbstractItemRelationView;
import com.usc.app.view.search.ClassViewNodeReplaceSql;
import com.usc.obj.util.USCObjExpHelper;
import com.usc.server.jdbc.DBUtil;

public class RealtionClassViewGetNodeData extends AbstractItemRelationView
{

	@Override
	public Object executeAction() throws Exception
	{
		String treenodepid = classViewNode.getTreenodepid();
		String treenodedata = classViewNode.getTreenodedata();
		String dataCondition = classViewNode.getDatacondition();
		dataCondition = USCObjExpHelper.parseObjValueInExpression(root,
				ClassViewNodeReplaceSql.replaceCNodeDataSql(context, dataCondition, treenodedata, treenodepid));
		List list = DBUtil.getSQLResultByConditionLimit(context.getItemInfo(), dataCondition, getPage());
		return queryTrue(list);
	}
}
