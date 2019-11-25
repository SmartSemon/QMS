package com.usc.app.query.search;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.app.query.search.i.AbstractSearchAction;
import com.usc.app.util.SearchUtils;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.field.FieldNameInitConst;

public class SearchRelationPageAction extends AbstractRelationAction implements AbstractSearchAction
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
		String relationTn = getRelationShip().getrelationTableName();
		String condition = "del=0 AND EXISTS(SELECT 1 FROM " + relationTn + " WHERE del=0 AND "
				+ FieldNameInitConst.FIELD_ITEMBID + "=" + itemInfo.getTableName() + ".id AND "
				+ FieldNameInitConst.FIELD_ITEMB + "='" + itemInfo.getItemNo() + "' AND "
				+ FieldNameInitConst.FIELD_ITEMA + "='" + root.getItemNo() + "' AND " + FieldNameInitConst.FIELD_ITEMAID
				+ "='" + root.getID() + "')";
		return queryTrue(
				SearchUtils.searchByCondition(itemInfo, getQueryWord(context), condition, getDataPage(context)));
	}

}
