package com.usc.server.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.datetime.DateFormatter;

import com.usc.util.ObjectHelperUtils;

public abstract class SystemTime
{
	public final static DateFormatter formatter = new DateFormatter("yyyy-MM-dd HH:mm:ss");

	public static Timestamp getTimestamp()
	{
		return new Timestamp(new Date().getTime());
	}

	public static Timestamp getTimestamp(String timeString)
	{
		if (ObjectHelperUtils.isEmpty(timeString))
		{
			return null;
		}
		Date date = null;
		try
		{
			date = formatter.parse(timeString, Locale.getDefault());
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return new Timestamp(date.getTime());
	}

	public static Timestamp getTimestamp(String format, String timeFormat)
	{
		if (ObjectHelperUtils.isEmpty(timeFormat))
		{
			return null;
		}
		DateFormatter formatter = new DateFormatter(format);
		Date date = null;
		try
		{
			date = formatter.parse(timeFormat, Locale.getDefault());
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return new Timestamp(date.getTime());
	}

}
