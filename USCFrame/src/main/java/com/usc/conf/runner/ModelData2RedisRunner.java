package com.usc.conf.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.usc.server.AppRunner;
import com.usc.server.init.InitModelData;
import com.usc.server.util.LoggerFactory;

@Component
@Order(1)
public class ModelData2RedisRunner extends AppRunner
{

	public void run(ApplicationArguments args) throws Exception
	{
		try
		{
			InitModelData.initModel();
			LoggerFactory.logInfo(this.getClass().getCanonicalName() + "  [Init Success]");
		} catch (Exception e)
		{
			LoggerFactory.logError("Init Failed", e);
		}

	}

}
