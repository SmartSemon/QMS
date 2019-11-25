package com.usc.app.action.task.query;

import com.usc.app.query.a.AbstractQueryItemRelationDataAction;
import com.usc.obj.api.type.task.TaskUtil;

public class GetTaskInOrOutputData extends AbstractQueryItemRelationDataAction
{

	@Override
	public Object executeAction() throws Exception
	{
		return queryTrue(TaskUtil.getTaskInOrOutputData(context, root, getPage()));
	}

}
