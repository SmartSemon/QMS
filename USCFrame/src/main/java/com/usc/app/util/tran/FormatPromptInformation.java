package com.usc.app.util.tran;

import java.util.HashMap;
import java.util.Map;

public class FormatPromptInformation
{
	private static Map<String, String> loginMsg = new HashMap<String, String>();
	private static Map<String, String> authorMsg = new HashMap<String, String>();
	private static Map<String, String> systemLogMsg = new HashMap<String, String>();
	private static Map<String, String> systemUserMsg = new HashMap<String, String>();
	private static Map<String, String> systemCodeMsg = new HashMap<String, String>();

	public static String getLoginMsg(String key)
	{
		if (loginMsg.containsKey(key))
		{
			return loginMsg.get(key);
		}
		String msg = FormatUtils.getValue(key, FormatUtils.getLoginPath());
		if (msg != null)
		{
			loginMsg.put(key, msg);
		}
		return msg;

	}

	public static String getSystemCodeMsg(String key)
	{
		if (systemCodeMsg.containsKey(key))
		{
			return systemCodeMsg.get(key);
		}
		String msg = FormatUtils.getValue(key, FormatUtils.getSystemcodePath());
		if (msg != null)
		{
			systemCodeMsg.put(key, msg);
		}
		return msg;

	}

	public static String getUserMsg(String key)
	{
		if (systemUserMsg.containsKey(key))
		{
			return systemUserMsg.get(key);
		}
		String msg = FormatUtils.getValue(key, FormatUtils.getSystemuserPath());
		if (msg != null)
		{
			systemUserMsg.put(key, msg);
		}
		return msg;

	}

	public static String getSystemLogMsg(String key)
	{
		if (systemLogMsg.containsKey(key))
		{
			return systemLogMsg.get(key);
		}
		String msg = FormatUtils.getValue(key, FormatUtils.getLoginPath());
		if (msg != null)
		{
			systemLogMsg.put(key, msg);
		}
		return msg;

	}

	public static String getAuthorMsg(String key)
	{
		if (authorMsg.containsKey(key))
		{
			return loginMsg.get(key);
		}
		String msg = FormatUtils.getValue(key, FormatUtils.getAuthorPath());
		if (msg != null)
		{
			loginMsg.put(key, msg);
		}
		return msg;

	}
}
