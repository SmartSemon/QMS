package com.usc.conf.cf.polltask.bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ObjectUtils;

import com.usc.conf.cf.polltask.task.PollTaskJob;

public class PollTaskJobFactory
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PollTaskJobFactory.class);

	public static Map<String, PollTaskJob> initTaskJobs(JdbcTemplate jdbcTemplate)
	{
		LOGGER.info(">>>>>>>>开始初始化POLLTASK>>>>>>>>");
		PollTaskMapper taskMapper = new PolllTaskMapperImpl();
		List<PollTaskBean> beans = taskMapper.getAllTask(jdbcTemplate);
		Map<String, PollTaskJob> taskMap = new ConcurrentHashMap<String, PollTaskJob>();
		if (!ObjectUtils.isEmpty(beans))
		{
			beans.forEach(bean -> {
				String impl = bean.getImplclass();
				if (impl != null)
				{
					Class<?> localClass = null;
					try
					{
						localClass = Class.forName(impl);
						if (localClass != null)
						{
							PollTaskJob localObject = (PollTaskJob) localClass.newInstance();
							taskMap.put(impl, localObject);
						}
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
					{
						e.printStackTrace();
					}

				}
			});
		}
		LOGGER.info(">>>>>>>>>初始化POLLTASK结束>>>>>>>>");
		return taskMap;
	}

}
