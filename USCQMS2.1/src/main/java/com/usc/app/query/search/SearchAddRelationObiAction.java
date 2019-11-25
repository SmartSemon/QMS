package com.usc.app.query.search;

import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.query.search.i.AbstractSearchAction;
import com.usc.app.util.SearchUtils;
import com.usc.server.md.ItemInfo;

public class SearchAddRelationObiAction extends AbstractAction implements AbstractSearchAction
{

	@Override
	public Object executeAction() throws Exception
	{
		ItemInfo info = context.getItemInfo();
		if (info == null)
		{
			return null;
		}
		List<Map> rData = (List<Map>) context.getExtendInfo("rData");
		Map<String, Object> map = SearchUtils.getAddRelationPageDataCondition(rData);
		String condition = "del=0";
		Object[] objects = null;
		int[] types = null;
		if (map != null)
		{
			condition = condition + " AND " + map.get("condition");
			objects = (Object[]) map.get("objects");
			types = (int[]) map.get("types");
		}
		return queryTrue(SearchUtils.searchByCondition(info, getQueryWord(context), condition, objects, types,
				getDataPage(context)));
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
