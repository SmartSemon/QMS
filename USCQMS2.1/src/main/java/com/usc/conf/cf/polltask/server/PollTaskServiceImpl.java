package com.usc.conf.cf.polltask.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.usc.conf.cf.polltask.bean.PollTaskBean;
import com.usc.conf.cf.polltask.bean.PollTaskMapper;
import com.usc.conf.cf.polltask.task.PollTaskJob;

@SuppressWarnings("rawtypes")
@Service("pollTaskService")
public class PollTaskServiceImpl implements PollTaskService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PollTaskServiceImpl.class);
	@Autowired
	private PollTaskMapper taskMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	private ReentrantLock lock = new ReentrantLock();

	@Autowired
	@Qualifier(value = "scheduledTaskJobMap")
	private Map<String, PollTaskJob> scheduledTaskJobMap;

	private Map<String, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

	@Override
	synchronized public List<PollTaskBean> taskList()
	{

		List<PollTaskBean> taskBeanList = taskMapper.getAllTask(jdbcTemplate);
		if (CollectionUtils.isEmpty(taskBeanList))
		{
			return new ArrayList<>();
		}

		for (PollTaskBean taskBean : taskBeanList)
		{
			String impl = taskBean.getImplclass();
			taskBean.setIsenable(this.isStart(impl));
		}
		return taskBeanList;
	}

	@Override
	synchronized public Boolean start(String implclass)
	{
		lock.lock();
		try
		{
			if (this.isStart(implclass))
			{
				LOGGER.info(">>>>>> 当前事务已经启动，无需重复启动！");
				return false;
			}
			if (!scheduledTaskJobMap.containsKey(implclass))
			{
				LOGGER.info(">>>>>> 轮询事务实现类{" + implclass + "}不存在，启动失败！");
				return false;
			}
			PollTaskBean pollTask = taskMapper.getByImplclass(jdbcTemplate, implclass);
			this.doStartTask(pollTask);
			LOGGER.info(">>>>>> 启动事务 {" + pollTask.getName() + "->" + implclass + "} 完成 >>>>>>", implclass);
		} finally
		{
			lock.unlock();
		}

		return true;
	}

	@Override
	synchronized public Boolean stop(String implclass)
	{
		boolean taskStartFlag = scheduledFutureMap.containsKey(implclass);
		if (taskStartFlag)
		{
			ScheduledFuture scheduledFuture = scheduledFutureMap.get(implclass);
			scheduledFuture.cancel(true);
		}
		LOGGER.info(">>>>>> 已停止事务 {" + implclass + "}  >>>>>>");
		return taskStartFlag;
	}

	@Override
	synchronized public Boolean restart(String implclass)
	{
		LOGGER.info(">>>>>> 进入重启事务 实现类 {" + implclass + "}  >>>>>>");
		this.stop(implclass);
		return this.start(implclass);
	}

	@Override
	synchronized public void initAllTask(List<PollTaskBean> pollTaskBeanList)
	{
		LOGGER.info("程序启动 ==> 初始化所有事务开始 ！size={" + pollTaskBeanList.size() + "}");
		if (CollectionUtils.isEmpty(pollTaskBeanList))
		{
			return;
		}
		for (PollTaskBean pollTask : pollTaskBeanList)
		{
			String implclass = pollTask.getImplclass();
			if (this.isStart(implclass))
			{
				continue;
			}
			this.doStartTask(pollTask);
		}

	}

	private void doStartTask(PollTaskBean pollTask)
	{
		String implclass = pollTask.getImplclass();
		String polltime = pollTask.getPolltime();
		PollTaskJob pollTaskJob = scheduledTaskJobMap.get(implclass);
		ScheduledFuture scheduledFuture = threadPoolTaskScheduler.schedule(pollTaskJob, new Trigger()
		{
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext)
			{
				CronTrigger cronTrigger = new CronTrigger(polltime);
				return cronTrigger.nextExecutionTime(triggerContext);
			}
		});

		scheduledFutureMap.put(implclass, scheduledFuture);
	}

	private Boolean isStart(String implclass)
	{
		if (scheduledFutureMap.containsKey(implclass))
		{
			if (!scheduledFutureMap.get(implclass).isCancelled())
			{
				return true;
			}
		}
		return false;
	}

}
