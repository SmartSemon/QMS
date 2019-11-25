package com.usc.obj.api;

import java.io.Serializable;

public abstract interface USCDisPlayAble extends Serializable
{
	public abstract boolean isDisplayable(InvokeContext paramInvokeContext);
}
