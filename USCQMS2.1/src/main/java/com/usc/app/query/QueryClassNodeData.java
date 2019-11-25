package com.usc.app.query;

import java.util.List;

import com.usc.app.action.a.AbstractClassAction;
import com.usc.server.jdbc.ItemUtiilities;

public class QueryClassNodeData extends AbstractClassAction
{

	@Override
	public Object executeAction() throws Exception
	{
		String nodeItemNo = super.getClassNodeItemNo();
		String itemno = context.getItemNo();
		List list = ItemUtiilities.getClasNodeData(nodeItemNo, itemno);
		return this.queryTrue(list);
	}

}
