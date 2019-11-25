package com.usc.app.view;

import com.usc.app.view.a.AbstractItemRelationView;
import com.usc.test.mate.action.MGetClassViewModelData;

public class GetRelationClassViewTreeNodeData extends AbstractItemRelationView
{

	@Override
	public Object executeAction() throws Exception
	{

		return queryTrue(MGetClassViewModelData.getClassViewModelData(classView.getNo(), root).getClassViewNodeList());
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
