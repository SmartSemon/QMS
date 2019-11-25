package com.usc.app.action.i;

import com.usc.obj.api.impl.ApplicationContext;

public abstract interface AppAction extends ICommandEnvironmentAccess, ReturnRequest
{
	public abstract Object action() throws Exception;

	public abstract boolean isEnabled() throws Exception;

	public abstract void setApplicationContext(ApplicationContext context);

	public abstract ApplicationContext getApplicationContext();
}
