package com.usc.obj.api.type.task;

import com.usc.app.action.cache.OperationalCach;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.impl.DefaultAbstractObjAction;

public class TaskOutputObjectAction extends DefaultAbstractObjAction
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isDeleteAble(InvokeContext context) throws Exception
	{
		return true;
	}

	@Override
	public boolean delete(InvokeContext context) throws Exception
	{
		if (isDeleted(context))
		{
			return false;
		}
		if (!isDeleteAble(context))
		{
			return false;
		}
		Boolean f = super.delete(context);
		if (f)
		{
			OperationalCach.putMObj(this.currObj);
		}

		return f;
	}
}
