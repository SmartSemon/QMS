package com.usc.app.view.search;

import com.usc.app.query.search.i.AbstractSearchAction;
import com.usc.app.util.SearchUtils;
import com.usc.app.view.a.AbstractItemView;
import com.usc.server.md.ItemInfo;

public class SearchClassViewNodeObjectAction extends AbstractItemView implements AbstractSearchAction
{

	@Override
	public Object executeAction() throws Exception
	{
		ItemInfo itemInfo = context.getItemInfo();
		if (itemInfo == null)
		{
			return failedOperation("Business objects not exists");
		}
		if (!hasQueryFields(itemInfo))
		{
			return failedOperation("This object No queryFields");
		}
		String treenodepid = classViewNode.getTreenodepid();
		String treenodedata = classViewNode.getTreenodedata();
		String dataCondition = classViewNode.getDatacondition();
		dataCondition = ClassViewNodeReplaceSql.replaceCNodeDataSql(context, dataCondition, treenodedata, treenodepid);

		return queryTrue(
				SearchUtils.searchByCondition(itemInfo, getQueryWord(context), dataCondition, getDataPage(context)));
	}

}
