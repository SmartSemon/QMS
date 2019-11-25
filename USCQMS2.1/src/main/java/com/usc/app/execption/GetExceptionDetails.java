package com.usc.app.execption;

import java.io.PrintWriter;
import java.io.StringWriter;

public class GetExceptionDetails
{
	public static String details(Throwable e)
	{
		if (e == null)
		{
			return "no exception details";
		}
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		e.printStackTrace(printWriter);
		String es = writer.toString();
		try
		{
			return es;
		} finally
		{
			e.printStackTrace();
		}

	}
}
