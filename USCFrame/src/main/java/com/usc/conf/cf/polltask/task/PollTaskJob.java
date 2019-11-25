package com.usc.conf.cf.polltask.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.a.DefaultInvokeContext;
import com.usc.obj.util.ApplicationCreator;

public abstract class PollTaskJob implements Runnable
{
	public static final Logger LOGGER = LoggerFactory.getLogger(PollTaskJob.class);

	@Override
	public void run()
	{
		doTaskThings();
	}

	public abstract void doTaskThings();

	public DefaultInvokeContext createSyatemConext(USCObject... objects)
	{
		return ApplicationCreator.newContext().createSyatemConext(null, objects);

	}

}
