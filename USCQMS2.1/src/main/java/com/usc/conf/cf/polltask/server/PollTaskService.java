package com.usc.conf.cf.polltask.server;

import java.util.List;

import com.usc.conf.cf.polltask.bean.PollTaskBean;

public interface PollTaskService
{
	List<PollTaskBean> taskList();

	Boolean start(String implclass);

	Boolean stop(String implclass);

	Boolean restart(String implclass);

	void initAllTask(List<PollTaskBean> pollTaskBeanList);
}
