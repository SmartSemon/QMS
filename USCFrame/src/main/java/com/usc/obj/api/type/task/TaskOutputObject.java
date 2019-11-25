package com.usc.obj.api.type.task;

import java.util.HashMap;
import java.util.Map;

import com.usc.app.action.cache.OperationalCach;
import com.usc.app.execption.ThrowRemoteException;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCHierarchyObject;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.type.GeneralObject;
import com.usc.obj.util.USCObjectQueryHelper;
import com.usc.util.ObjectHelperUtils;

public class TaskOutputObject extends GeneralObject implements USCHierarchyObject
{

	/**
	 *
	 */
	private static final long serialVersionUID = -5616391473219609524L;

	public TaskOutputObject(String objType, HashMap<String, Object> map)
	{
		super(objType, map);
	}

	@Override
	public boolean delete(InvokeContext context) throws Exception
	{
		if (!isDeleteAble(context))
		{
			return false;
		}
		boolean b = false;
		if (!isDeleted(context))
		{
			USCObjectAction objectAction = context.getActionObjType();
			objectAction.setCurrObj(this);
			b = objectAction.delete(context);
			if (b)
			{
				setObj2Deleted();
				OperationalCach.putDeleteObj(this);
			}
		}

		return b;
	}

//	@Override
//	public boolean isDeleteAble(InvokeContext context) throws Exception
//	{
//		return isEnable() && super.isDeleteAble(context);
//	}
//
//	public boolean isEnable()
//	{
//		boolean b = false;
//		String tstate = getTaskState();
//		if ("A".equals(tstate) || "E".equals(tstate))
//		{
//			b = true;
//		}
//		return b;
//	}
//
	@Override
	public boolean save(InvokeContext context)
	{
		ThrowRemoteException.throwException(new RuntimeException("Do not modify task input"));
		return false;
	}

	private TaskOutputObject[] inputs;

	@Override
	public boolean hasChildren()
	{
		String pid = this.getFieldValueToString("pid");
		if ("0".equals(pid))
		{
			this.inputs = (TaskOutputObject[]) getChildren();
			if (!ObjectHelperUtils.isEmpty(inputs))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public USCObject[] getChildren()
	{
		if (ObjectHelperUtils.isEmpty(inputs))
		{
			USCObject[] children = USCObjectQueryHelper.getObjectsByCondition(getItemNo(), getChildrenCondition(this));
			if (children != null)
			{
				inputs = (TaskOutputObject[]) children;
			}
		}

		return inputs;
	}

	@Override
	public USCObject[] createChildren(ApplicationContext context, Map<String, Object>... childrenDatas)
	{
		return null;
	}

}
