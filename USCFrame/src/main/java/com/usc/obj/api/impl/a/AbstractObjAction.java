package com.usc.obj.api.impl.a;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObjectAction;

public abstract class AbstractObjAction implements USCObjectAction
{

	/**
	 *
	 */
	private static final long serialVersionUID = -7213716706977171962L;

	public Object checkOutObj(InvokeContext context)
	{
		return false;
	}

	public Object canCheckOutObj(InvokeContext context)
	{
		return false;
	}

}
