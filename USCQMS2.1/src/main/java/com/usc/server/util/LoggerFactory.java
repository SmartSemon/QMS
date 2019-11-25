package com.usc.server.util;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.usc.Application;

public class LoggerFactory {
	public static final Log logger = LogFactory.getLog(Application.class);

	public static void logInfo(Class clazz,String msg) {
		Log logger = LogFactory.getLog(clazz.getClass());
		logger.info(msg);
	}

	public static void logInfo(String msg) {
		logger.info(msg);
	}
	public static void logError(Class clazz,String msg) {
		Log logger = LogFactory.getLog(clazz.getClass());
		logger.info(msg);
	}
	public static void logError(String msg) {
		logger.error(msg);
	}

	public static void logError(Class clazz,String msg,Throwable e) {
		Log logger = LogFactory.getLog(clazz.getClass());
		logger.error(msg, e);
	}

	public static void logError(String msg,Throwable e) {
		logger.error(msg, e);
	}

}
