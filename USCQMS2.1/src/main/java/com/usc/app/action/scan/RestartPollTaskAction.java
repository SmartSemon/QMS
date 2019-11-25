package com.usc.app.action.scan;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.action.a.AbstractAction;
import com.usc.app.action.utils.ActionMessage;
import com.usc.app.util.http.HttpURLConnectionUtils;
import com.usc.obj.api.USCObject;
import com.usc.server.util.SystemTime;

@RestController
public class RestartPollTaskAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject object = context.getSelectedObj();
		String clazz = object.getFieldValueToString("implclass");
		if (clazz != null)
		{
			String url = "http://localhost:8899/poll/restart";
			Map<String, Object> impl = new JSONObject();
			impl.put("implclass", clazz);
			if (HttpURLConnectionUtils.sendPostRequest(url, impl).equals("true"))
			{
				object.setFieldValue("rstime", SystemTime.getTimestamp());
				object.save(context);
				return new ActionMessage(flagTrue, "M", "重启轮询任务成功", null, object);
			}
		}
		return failedOperation();
	}

	@Override
	public boolean disable() throws Exception
	{
		// 需要验证用户是否为系统管理员
		USCObject[] objects = context.getSelectObjs();
		for (USCObject uscObject : objects)
		{
			if (!(boolean) uscObject.getFieldValue("isenable"))
			{
				return true;
			}
		}
		return false;
	}

}
