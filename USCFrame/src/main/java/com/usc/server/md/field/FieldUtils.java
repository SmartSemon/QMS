package com.usc.server.md.field;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;

import com.alibaba.fastjson.JSON;
import com.usc.server.md.ItemField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldUtils
{
	public static Object getObjectByType(Object object, ItemField field) throws Exception
	{
		if (object == null)
			return null;

		String strFieldType = field.getFType();
		if (strFieldType == null)
		{

			if (object instanceof String)
			{
				return ((String) object).trim();
			}
			if (object.getClass().getName().equals("java.util.Timestamp"))
			{
				return (Timestamp) fromSqlTimestamp(object);
			}
			return object;
		}
		try
		{
			String editor = field.getEditor();
			if (FieldEditor.MAPVALUELIST.equals(editor))
			{
				return field.getFieldEditor().getMapValueList().get(object);
			}
			if (FieldAdapter.isInt(strFieldType))
			{
				if (object instanceof Boolean)
				{
					return (Boolean) object;
				}
				return Integer.valueOf(String.valueOf(object));
			}
			if (FieldAdapter.isVarchar(strFieldType))
			{
				return (String.valueOf(object)).trim();
			}
			if (FieldAdapter.isFloat(strFieldType))
			{
				return Float.valueOf(String.valueOf(object));
			}
			if (FieldAdapter.isDouble(strFieldType))
			{
				return Double.valueOf(String.valueOf(object));
			}
			if (FieldAdapter.isNumeric(strFieldType))
			{
				return (object != null) ? object : null;
			}
			if (FieldAdapter.isBoolean(strFieldType))
			{
				if (object instanceof Boolean)
				{
					return object;
				}
				if (object instanceof Integer)
				{
					return BooleanUtils.toBoolean((int) object);
				}
				if (object instanceof String)
				{
					return BooleanUtils.toBoolean((String) object);
				}
				return false;
			} else if (FieldAdapter.isDateTime(strFieldType))
			{
				return dateFieldValueToString(object, field);
			} else
			{
				return object;
			}
		} catch (Exception e)
		{
			log.error("Field translate Error :" + field, e);
		}

		return null;

	}

	public static Object getObjectByType(ResultSet rest, int nIndex, ItemField field) throws Exception
	{
		if (rest == null)
			return null;

		Object object = rest.getObject(nIndex);
		return getObjectByType(object, field);

	}

	public static Timestamp fromSqlTimestamp(Object value) throws Exception
	{
		if (value == null)
		{
			return null;
		}

		Class<?> clz = value.getClass();
		Method method = clz.getMethod("timestampValue", new Class[0]);
		return (Timestamp) method.invoke(value, new Object[0]);
	}

	public static String dateFieldValueToString(Object object, ItemField field)
	{
		try
		{
			return (object == null || field == null) ? null
					: transFormatTimeToString((Date) object, field.getFieldEditor().getDateFormatter());
		} catch (Exception e)
		{
			if (object instanceof String)
			{
				return (String) object;
			}
			if (object instanceof Number)
			{
				Long _long = ((Number) object).longValue();
				Date time = new Date(_long);
				return transFormatTimeToString(time, field.getFieldEditor().getDateFormatter());
			}
		}
		return null;
	}

	public static String transFormatTimeToString(Date time, String format)
	{
		return JSON.toJSONStringWithDateFormat(time, format).replace("\"", "");
	}
}
