package com.usc.conf.cf.polltask.task;

import java.sql.Timestamp;
import java.util.Date;

public class MyTask4 extends PollTaskJob
{

	@Override
	public void doTaskThings()
	{

		Timestamp timestamp = new Timestamp(new Date().getTime());
		String t = timestamp.toString();
		PollTaskJob.LOGGER.info(this.getClass().getName() + "-----------------开始了-------------------" + "[" + t + "]");
	}

}
