package com.usc.obj.api.type.task.i;

import com.usc.obj.api.USCObject;

public interface TaskRelationInfo
{
	public abstract USCObject[] getTaskInputBusinessItems();

	public abstract USCObject[] getTaskInputObjs();

	public abstract USCObject[] getTaskOutputObjs();
}
