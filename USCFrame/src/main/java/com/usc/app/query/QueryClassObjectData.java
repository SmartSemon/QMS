package com.usc.app.query;

import java.util.List;

import com.usc.app.action.a.AbstractClassAction;
import com.usc.server.jdbc.ItemUtiilities;

public class QueryClassObjectData extends AbstractClassAction
{

	@Override
	public Object executeAction() throws Exception
	{
		if (!super.disable())
		{
			String nodeID = nodeObj.getID();
			String itemNo = context.getItemNo();
			String classTable = super.getClassItemInfo().getTableName();

			List dataList = ItemUtiilities.getClasObjectResult(nodeID, itemNo, classTable);
			return this.queryTrue(dataList);
		}
		return this.queryFalse(null);
	}

}
