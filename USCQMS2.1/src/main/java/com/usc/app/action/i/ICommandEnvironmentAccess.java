package com.usc.app.action.i;

import com.usc.obj.api.impl.ApplicationContext;

public interface ICommandEnvironmentAccess
{
	public static final String USE_LOCATION_KEY = ICommandEnvironmentAccess.class.getName() + "LOCATION";
	public static final String USE_LOCATION_PopuMenu = "1";
	public static final String USE_LOCATION_View = "2";
	public static final String USE_LOCATION_Toolbar = "3";

	public abstract boolean isCommandEnvironmentEnable(ApplicationContext paramAPPInvokeContext);
}
