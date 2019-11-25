package com.usc.conf.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.usc.server.AppRunner;
import com.usc.server.init.InitJurisdictionData;
import com.usc.server.util.LoggerFactory;

/**
 *
 * <p>
 * Description: 授权信息初始化
 * </P>
 *
 * @date 2019年9月17日
 * @author SEMON
 */
@Component
@Order(4)
public class FunctionalPermissionsRunner extends AppRunner
{

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		InitJurisdictionData.init(jdbcTemplate);
		LoggerFactory.logInfo(InitJurisdictionData.class.getName() + " [Init Success]");
	}

}
