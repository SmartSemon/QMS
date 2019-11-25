package com.usc.obj.api.type.task;

import java.util.Date;
import java.util.HashMap;

import com.usc.app.action.cache.OperationalCach;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.api.type.GeneralObject;
import com.usc.obj.api.type.task.i.TaskRelationInfo;

public class TaskObject extends GeneralObject implements TaskRelationInfo
{

	/**
	 *
	 */
	private static final long serialVersionUID = -5616391473219609524L;

	public TaskObject(String objType, HashMap<String, Object> map)
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

	@Override
	public boolean isDeleteAble(InvokeContext context) throws Exception
	{
		return isEnable() && super.isDeleteAble(context);
	}

	public boolean isEnable()
	{
		boolean b = false;
		String tstate = getTaskState();
		if ("A".equals(tstate) || "E".equals(tstate))
		{
			b = true;
		}
//		else
//		{
//			LoggerFactory.logError("Task state is not 'A' or 'E', do not operate this task");
//		}
		return b;
	}

	@Override
	public boolean save(InvokeContext context)
	{
		boolean b = false;
		StackTraceElement[] s = new Exception().getStackTrace();
		if (s[1].getClassName().equals("com.usc.app.action.BatchModifyAction"))
		{
			b = isEnable() ? super.save(context) : false;
		} else
		{
			b = super.save(context);
		}
		if (this.getTaskState().equals("F") || this.getTaskState().equals("Z"))
		{
			b = false;
		}
		return b;
	}

	public String getTaskState()
	{
		Object task_status = this.getFieldValue("TSTATE");
		String tstate = String.valueOf(task_status);
		return tstate;
	}

	public String getgetTaskStateToString()
	{
		return this.getFieldValueToString("TSTATE");
	}

	public String getTaskType()
	{
		return this.getFieldValueToString("TTYPE");
	}

	public String getGrade()
	{
		return this.getFieldValueToString("TGRADE");
	}

	public Date getETime()
	{
		return (Date) this.getFieldValue("ETIME");
	}

	public String getLeader()
	{
		return this.getFieldValueToString("LEADER");
	}

	public String getExecutor()
	{
		return this.getFieldValueToString("EXECUTOR");
	}

	public String getDLUser()
	{
		return this.getFieldValueToString("DLUSER");
	}

	public Date getDLTime()
	{
		return (Date) this.getFieldValue("DLTIME");
	}

	public String getSTUser()
	{
		return this.getFieldValueToString("STUSER");
	}

	public Date getSTTime()
	{
		return (Date) this.getFieldValue("STTIME");
	}

	public Date getFNSTime()
	{
		return (Date) this.getFieldValue("FNSTIME");
	}

	public Date getCFTime()
	{
		return (Date) this.getFieldValue("CFTIME");
	}

	@Override
	public USCObject[] getTaskInputBusinessItems()
	{
		return TaskUtil.getInputBusinessItems(this.getID());
	}

	@Override
	public USCObject[] getTaskInputObjs()
	{

		return TaskUtil.getInputs(this.getID());
	}

	@Override
	public USCObject[] getTaskOutputObjs()
	{
		return null;
	}

}
