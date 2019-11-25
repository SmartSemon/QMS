package com.usc.obj.api.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class USCObjectDynamicProxy implements InvocationHandler
{

	private Object object;

	public USCObjectDynamicProxy(Object object)
	{
		this.object = object;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		return method.invoke(object, args);
	}

}
