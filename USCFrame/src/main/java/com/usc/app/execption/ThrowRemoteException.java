package com.usc.app.execption;

import java.rmi.RemoteException;

public class ThrowRemoteException
{
	public static RemoteException throwException(Throwable e)
	{
		return new RemoteException(e.getMessage(), e);
	}

	public static RemoteException throwExceptionDetails(Throwable e)
	{
		return new RemoteException(GetExceptionDetails.details(e), e);
	}
}
