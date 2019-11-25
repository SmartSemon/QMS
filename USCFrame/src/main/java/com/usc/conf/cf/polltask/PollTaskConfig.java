package com.usc.conf.cf.polltask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.usc.conf.cf.polltask.bean.PollTaskBean;
import com.usc.conf.cf.polltask.bean.PollTaskMapper;
import com.usc.conf.cf.polltask.bean.PolllTaskMapperImpl;
import com.usc.conf.cf.polltask.task.PollTaskJob;

@Component
@Configuration
public class PollTaskConfig
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PollTaskConfig.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler()
	{
		LOGGER.info("ST-创建任务调度池-START");
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(20);
		scheduler.setThreadNamePrefix("taskExecutor-");
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		scheduler.setAwaitTerminationSeconds(60);
		LOGGER.info("ST-创建任务调度池-END");

		return scheduler;
	}

	@Bean(name = "scheduledTaskJobMap")
	public Map<String, PollTaskJob> scheduledTaskJobMap()
	{
		return initTaskJobs();
	}

	public Map<String, PollTaskJob> initTaskJobs()
	{
		LOGGER.info("开始初始化POLLTASK");
		PollTaskMapper taskMapper = new PolllTaskMapperImpl();
		List<PollTaskBean> beans = taskMapper.getAllTask(jdbcTemplate);
		Map<String, PollTaskJob> taskMap = new ConcurrentHashMap<String, PollTaskJob>();
		if (!ObjectUtils.isEmpty(beans))
		{
			beans.forEach(bean -> {
				String impl = bean.getImplclass();
				if (impl != null)
				{
					try
					{
						Class<?> localClass = Class.forName(impl);
						if (localClass != null)
						{
							@SuppressWarnings("deprecation")
							PollTaskJob localObject = (PollTaskJob) localClass.newInstance();
							taskMap.put(impl, localObject);
						}
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
					{
						LOGGER.error("ERROR:!!!!!!!!!!!!!!!!!!>>>未找到轮询任务类>>>{" + impl + "}");
					}

				}
			});
		}
		LOGGER.info("初始化POLLTASK结束");
		return taskMap;
	}
}
