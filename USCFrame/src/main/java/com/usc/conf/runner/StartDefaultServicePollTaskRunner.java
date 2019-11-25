package com.usc.conf.runner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.usc.app.action.mate.MateFactory;
import com.usc.conf.cf.polltask.bean.PollTaskBean;
import com.usc.conf.cf.polltask.server.PollTaskService;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.impl.ObjectCachingDataHelper;
import com.usc.obj.util.USCObjectQueryHelper;
import com.usc.server.AppRunner;

@Component
@Order(10)
public class StartDefaultServicePollTaskRunner extends AppRunner
{
	@Autowired
	private PollTaskService pollTaskService;

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		List<PollTaskBean> tasks = pollTaskService.taskList();
		if (!CollectionUtils.isEmpty(tasks))
		{
			tasks.forEach(task -> {
				String impl = task.getImplclass();
				USCObject[] objects = USCObjectQueryHelper.getObjectsByCondition("SPOLLTASK",
						"del=0 AND implclass='" + impl + "'");
				USCObject taskObject = objects[0];
				Boolean isenable = (Boolean) taskObject.getFieldValue("isenable");
				ApplicationContext context = getConext(taskObject);
				if (task.isSelfstart())
				{
					if (pollTaskService.start(impl))
					{
						if (!isenable)
						{
							taskObject.setFieldValue("isenable", true);
							taskObject.save(context);
						}
					}
				} else
				{
					if (isenable)
					{
						taskObject.setFieldValue("isenable", false);
						taskObject.save(context);
					}
				}
			});
		}
	}

	private ApplicationContext getConext(USCObject taskObject)
	{
		ApplicationContext context = new ApplicationContext("admin", taskObject);
		USCObjectAction objectAction = (USCObjectAction) ObjectCachingDataHelper
				.newInstance(MateFactory.getItemInfo("SPOLLTASK").getImplClass() + "Action");
		context.setActionObjType(objectAction);

		return context;
	}

}
