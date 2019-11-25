package com.usc.obj.api.type.file;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.impl.DefaultAbstractObjAction;

public class FileObjectAction extends DefaultAbstractObjAction
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

		return super.delete(context);
	}
}
