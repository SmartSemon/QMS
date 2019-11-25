package com.usc.server.syslog;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.usc.server.jdbc.DBUtil;
import com.usc.server.util.BeanConverter;

@Service("loggingServer")
public class SLOGGingServiceImpl implements LoggingServer
{

	@Override
	public void save(SLOGObject slogObject)
	{
		Map<String, Object> log = BeanConverter.toMap(slogObject);
		try
		{
//			ItemUtiilities
			DBUtil.insertRecord("SYSLOG", log, slogObject.getCuser());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
