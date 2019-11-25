package com.usc.obj.api.bean;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObject;

public interface USCObjCreator
{
	public abstract USCObject create(InvokeContext context) throws Exception;

	public abstract boolean createAble(InvokeContext context) throws Exception;

	public abstract String getOnlyFields(InvokeContext context) throws Exception;
}
