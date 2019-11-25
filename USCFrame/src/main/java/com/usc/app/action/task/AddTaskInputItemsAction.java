package com.usc.app.action.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.type.task.TaskUtil;
import com.usc.util.ObjectHelperUtils;

public class AddTaskInputItemsAction extends AbstractRelationAction
{

	@Override
	public Object executeAction() throws Exception
	{
		if (!TaskUtil.checkLeader(context, root))
		{
			return failedOperation("你不是任务创建人或责任人，无权操作任务输入项");
		}

		if (TaskUtil.checkTaskState(context, root))
		{
			USCObject[] inputs = context.getSelectObjs();
			List<Map> maps = new ArrayList<Map>(inputs.length);
			if (ObjectHelperUtils.isEmpty(inputs))
			{
				return failedOperation("未选择任何对象");
			}
			for (int i = 0; i < inputs.length; i++)
			{
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("NO", inputs[i].getFieldValueBykey("NO"));
				hashMap.put("NAME", inputs[i].getFieldValueBykey("NAME"));
				hashMap.put("ITEMNO", inputs[i].getFieldValueBykey("NO"));
//				hashMap.put("ITEMTYPE", inputs[i].getFieldValue("ITEMTYPE"));
				hashMap.put("pid", "0");
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
