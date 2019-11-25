package com.usc.app.action.a;

import com.usc.app.action.i.AppAction;
import com.usc.app.query.rt.QueryReturnRequest;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;

public abstract class AbstractAction implements AppAction, QueryReturnRequest
{

	protected ApplicationContext context = null;

	public Object action() throws Exception
	{
		if (CheckUserConnections() && isCommandEnvironmentEnable(getApplicationContext()))
		{
			return executeAction();
		}
		return null;
	}

	public boolean isEnabled() throws Exception
	{
		return disable();
	}

	/**
	 * <p>
	 * Note: Specific transaction operations
	 *
	 * @return
	 * @throws Exception
	 */
	public abstract Object executeAction() throws Exception;

	/**
	 * <p>
	 * Note: Controlling the validity of transactions
	 *
	 * @return
	 * @throws Exception
	 */
	public abstract boolean disable() throws Exception;

	public ApplicationContext getApplicationContext()
	{
		return this.context;
	}

	public void setApplicationContext(ApplicationContext context)
	{
		this.context = context;
	}

	public USCObject getSelectedObj()
	{
		return context.getSelectedObj();
	}

	public boolean isCommandEnvironmentEnable(ApplicationContext context)
	{
		return true;
	}

	public boolean CheckUserConnections()
	{
		return true;
	}

}
