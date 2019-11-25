package com.usc.app.query.search.i;

import com.usc.app.query.rt.QueryReturnRequest;
import com.usc.app.util.SearchUtils;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.server.md.ItemInfo;
import com.usc.util.ObjectHelperUtils;

public interface AbstractSearchAction extends QueryReturnRequest
{
	public final String CONDITION = "condition";
	public final String QUERYWORD = "queryWord";
	public final String DATAPAGE = "page";

	default String getCondition(ApplicationContext context)
	{
		Object condition = context.getExtendInfo(CONDITION);
		return ObjectHelperUtils.isEmpty(condition) ? "del=0" : "del=0 AND " + condition.toString();

	}

	default String getQueryWord(ApplicationContext context)
	{
		String queryWord = (String) context.getExtendInfo(QUERYWORD);
		return queryWord;

	}

	default int getDataPage(ApplicationContext context)
	{
		Object object = context.getExtendInfo(DATAPAGE);
		int page = (Integer) (object == null ? 1 : object);
		return page;

	}

	default boolean hasQueryFields(ItemInfo itemInfo)
	{
		return SearchUtils.hasQueryFields(itemInfo);
	}
}
