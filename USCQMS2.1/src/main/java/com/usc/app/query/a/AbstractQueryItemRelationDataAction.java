package com.usc.app.query.a;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.app.query.search.i.AbstractSearchAction;

public abstract class AbstractQueryItemRelationDataAction extends AbstractRelationAction implements AbstractSearchAction
{
	protected Integer page;

	public Integer getPage()
	{
		if (page == null)
		{
			return (Integer) context.getExtendInfo("page");
		}
		return page;
	}

	public void setPage(Integer page)
	{
		this.page = page;
	}

	@Override
	public boolean disable() throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}

}
