package com.usc.obj.api;

import java.util.Map;

import com.usc.obj.api.impl.ApplicationContext;

public interface USCHierarchyObject
{
	public abstract boolean hasChildren();

	public abstract USCObject[] getChildren();

	public abstract USCObject[] createChildren(ApplicationContext context,
			@SuppressWarnings("unchecked") Map<String, Object>... childrenDatas);

	default String getChildrenCondition(USCObject object)
	{
		return "del=0 AND pid='" + object.getID() + "'";

	}
}
