package com.usc.app.action.task;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.action.task.util.TaskActionResult;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.type.task.TaskObject;
import com.usc.server.util.SystemTime;

public class ReceivingTaskAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject[] objects = context.getSelectObjs();
		for (USCObject uscObject : objects)
		{
			TaskObject taskObject = (TaskObject) uscObject;
			taskObject.setFieldValue("TSTATE", "C");
			taskObject.setFieldValue("GTIME", SystemTime.getTimestamp());
			taskObject.save(context);
		}

		return TaskActionResult.getResult(null, "Received_Task_Successfully", "D");
	}

	@Override
	public boolean disable() throws Exception
	{
		USCObject[] objects = context.getSelectObjs();
		if (objects == null)
		{
			return false;
		}
		for (USCObject uscObject : objects)
		{
			TaskObject taskObject = (TaskObject) uscObject;
			if (!taskObject.getTaskState().equals("B"))
			{
				return true;
			}
			if (!taskObject.getExecutor().equals(context.getUserInformation().getUserName()))
			{
				return true;
			}
		}
		return false;
	}

}
