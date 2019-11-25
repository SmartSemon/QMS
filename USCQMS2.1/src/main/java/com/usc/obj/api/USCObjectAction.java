package com.usc.obj.api;

public interface USCObjectAction extends USCDisPlayAble
{
	public abstract void setCurrObj(USCObject uscObject);

	public abstract boolean isDeleteAble(InvokeContext context) throws Exception;

	public abstract boolean isDeleted(InvokeContext context) throws Exception;

	public abstract boolean delete(InvokeContext context) throws Exception;

	public abstract boolean canClone(InvokeContext context) throws Exception;

	public abstract USCObject cloneObj(InvokeContext context) throws Exception;
}
