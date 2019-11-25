package com.usc.app.action.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.type.task.TaskUtil;

public class AddTaskInputObjectsAction extends AbstractRelationAction
{

	@Override
	public Object executeAction() throws Exception
	{
//		TaskObject task = (TaskObject) root;
		if (!TaskUtil.checkLeader(context, root))
		{
			return failedOperation("你不是任务创建人或责任人，无权操作任务输入项");
		}

		if (TaskUtil.checkTaskState(context, root))
		{

			USCObject[] objects = context.getSelectObjs();
			List<Map> maps = new ArrayList<Map>(objects.length);
			for (USCObject uscObject : objects)
			{
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("NO", uscObject.getFieldValueBykey("NO"));
				hashMap.put("NAME", uscObject.getFieldValueBykey("NAME"));
				hashMap.put("OBJECTID", uscObject.getFieldValueBykey("id"));
				hashMap.put("ITEMNO", uscObject.getItemNo());
//				hashMap.put("ITEMTYPE", uscObject.geti);
				hashMap.put("pid", uscObject.getFieldValueBykey("pid"));
				hashMap.put("TASKID", root.getID());
				context.setInitData(hashMap);
				USCObject object = context.createObj(context.getItemNo());
				maps.add(object.getFieldValues());
			}
			return createSuccessful(maps);

		} else
		{
			return failedOperation("任务状态为 [" + root.getFieldValueToString("TSTATE") + "]禁止操作任务输入项");
		}
	}

}
