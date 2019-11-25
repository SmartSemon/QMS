package com.usc.app.query.search;

import com.usc.app.action.a.AbstractClassAction;
import com.usc.app.query.search.i.AbstractSearchAction;
import com.usc.app.util.SearchUtils;
import com.usc.obj.api.type.ClassNodeObject;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.field.FieldNameInitConst;
import com.usc.server.util.LoggerFactory;

public class SearchClassObjectAction extends AbstractClassAction implements AbstractSearchAction
{

	@Override
	public Object executeAction() throws Exception
	{
		ItemInfo itemInfo = context.getItemInfo();
		if (itemInfo == null)
		{
			LoggerFactory.logError("Business objects noe exists");
			return failedOperation();
		}
		if (!hasQueryFields(itemInfo))
		{
			return failedOperation();
		}
		if (getClassItemInfo() == null)
		{
			LoggerFactory.logError("Classification object does not exist");
			return failedOperation();
		}

		ClassNodeObject nodeObj = (ClassNodeObject) super.nodeObj;
		String condition = "EXISTS(SELECT 1 FROM " + getClassItemInfo().getTableName() + " WHERE "
				+ FieldNameInitConst.FIELD_DEL + "=0 AND " + FieldNameInitConst.FIELD_ITEMID + "="
				+ itemInfo.getTableName() + "." + FieldNameInitConst.FIELD_ID + " AND "
				+ FieldNameInitConst.FIELD_NODEID + "='" + nodeObj.getID() + "')";

		return queryTrue(
				SearchUtils.searchByCondition(itemInfo, getQueryWord(context), condition, getDataPage(context)));
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
