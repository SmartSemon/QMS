package com.usc.app.view.search;

import com.usc.app.query.search.i.AbstractSearchAction;
import com.usc.app.util.SearchUtils;
import com.usc.app.view.a.AbstractItemRelationView;
import com.usc.obj.util.USCObjExpHelper;
import com.usc.server.md.ItemInfo;

public class SearchRelationClassViewNodeObjectAction extends AbstractItemRelationView implements AbstractSearchAction
{

	@Override
	public Object executeAction() throws Exception
	{
		ItemInfo itemInfo = context.getItemInfo();
		if (itemInfo == null)
		{
			return failedOperation();
		}
		if (!hasQueryFields(itemInfo))
		{
			return failedOperation("This object No queryFields");
		}
		String treenodepid = classViewNode.getTreenodepid();
		String treenodedata = classViewNode.getTreenodedata();
		String dataCondition = classViewNode.getDatacondition();
		dataCondition = USCObjExpHelper.parseObjValueInExpression(root,
				ClassViewNodeReplaceSql.replaceCNodeDataSql(context, dataCondition, treenodedata, treenodepid));
		dataCondition = USCObjExpHelper.parseObjValueInExpression(root, dataCondition);
		return queryTrue(
				SearchUtils.searchByCondition(itemInfo, getQueryWord(context), dataCondition, getDataPage(context)));
	}

}
