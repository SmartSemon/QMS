package com.usc.obj.api;

public abstract interface USCObjectProvider
{
	public abstract USCObject getSelectedObj();

	public abstract void setCurrObj(USCObject paramBCFObj);
}
