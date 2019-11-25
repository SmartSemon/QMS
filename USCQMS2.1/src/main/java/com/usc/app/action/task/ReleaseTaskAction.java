package com.usc.app.action.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.action.task.util.TaskActionResult;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.type.task.TaskObject;
import com.usc.server.util.SystemTime;

public class ReleaseTaskAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject[] objects = context.getSelectObjs();
		List<Map> maps = new ArrayList<Map>();
		for (USCObject taskObject : objects)
		{
			taskObject.setFieldValue("TSTATE", "B");
			taskObject.setFieldValue("DLUSER", context.getUserInformation().getUserName());
			taskObject.setFieldValue("DLTIME", SystemTime.getTimestamp());
			taskObject.save(context);
			maps.add(taskObject.getFieldValues());
		}

		return TaskActionResult.getResult(maps, "Delivered_Task_Successfully", "D");
	}

	@Override
	public boolean disable() throws Exception
	{
		USCObject[] objects = context.getSelectObjs();
		for (USCObject taskObject : objects)
		{
			if (!((TaskObject) taskObject).isEnable())
			{
				return true;
			}
		}
		return false;
	}

}
