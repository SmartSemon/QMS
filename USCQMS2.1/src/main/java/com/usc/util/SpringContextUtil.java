package com.usc.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringContextUtil implements ApplicationContextAware
{
	protected static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		context = applicationContext;
	}

	public static ApplicationContext getContext()
	{
		return context;
	}

	public static <T> T getBean(Class<T> clazz)
	{
		return context.getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> beanClass)
	{
		return context.getBean(name, beanClass);
	}
}
