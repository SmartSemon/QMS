package com.usc.app.query.search;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.app.query.search.i.AbstractSearchAction;
import com.usc.app.util.SearchUtils;
import com.usc.obj.util.USCObjExpHelper;
import com.usc.server.md.ItemInfo;

public class SearchRelationQueryViewAction extends AbstractRelationAction implements AbstractSearchAction
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
		String condition = getRelationQueryView().getWcondition();
		condition = USCObjExpHelper.parseObjValueInExpression(root, condition);
		return queryTrue(
				SearchUtils.searchByCondition(itemInfo, getQueryWord(context), condition, getDataPage(context)));
	}

}
