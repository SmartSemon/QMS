package com.usc.conf.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.usc.server.AppRunner;
import com.usc.server.init.InitUserInfo;
import com.usc.server.util.LoggerFactory;

@Component
@Order(2)
public class UserInfo2RedisRunner extends AppRunner
{

	@Override
	public void run(ApplicationArguments args) throws Exception
	{

		InitUserInfo.run(jdbcTemplate);
		LoggerFactory.logInfo(this.getClass().getCanonicalName() + "  [UserInfo Init Success]");
	}

}
