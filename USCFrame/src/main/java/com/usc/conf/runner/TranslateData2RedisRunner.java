package com.usc.conf.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.usc.cache.redis.RedisUtil;
import com.usc.server.AppRunner;
import com.usc.server.util.LoggerFactory;

@Component
@Order(3)
public class TranslateData2RedisRunner extends AppRunner
{

	RedisUtil redisUtil = new RedisUtil();

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		try
		{
			initTranslateData();
			LoggerFactory.logInfo(this.getClass().getSimpleName() + "  [Init Success]");
		} catch (Exception e)
		{
			LoggerFactory.logError(this.getClass().getSimpleName() + "Init Failed");
		}

	}

	private void propertice(File file)
	{
		Properties properties = new Properties();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			properties.load(br);
			br.close();
			Map<Object, Object> map = properties;
			redisUtil.set("ACTIONTRANSLATEMSG", map);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initTranslateData()
	{
		File f2 = new File(this.getClass().getResource("").getPath());
		File t = new File(f2.getPath() + "\\res\\actiontranslatemsg.properties");
		propertice(t);
	}

}
