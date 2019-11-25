package com.usc.app.action.i;

import com.usc.obj.api.USCObject;

public interface ChildrenAction
{
	public boolean hasChildren(USCObject object);

	public USCObject[] getChildren(USCObject object);
}
