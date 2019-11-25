package com.usc.obj.util;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.action.mate.MateFactory;
import com.usc.obj.api.USCObject;
import com.usc.server.jdbc.base.DataBaseUtils;
import com.usc.server.md.ItemField;
import com.usc.server.md.ItemInfo;
import com.usc.server.util.StringHelperUtil;

public class USCObjExpHelper
{
	public static String parseObjValueInExpression(ItemInfo info, JSONObject jsonObjectA, String paramString)
	{
		if (paramString == null || paramString.length() == 0)
			return null;
		if (jsonObjectA == null)
			return paramString;
		paramString = parseObjValueInExpression(info, jsonObjectA, "%OBJECT.", paramString);
		return paramString;

	}

	private static String parseObjValueInExpression(ItemInfo info, JSONObject jsonObject, String string,
			String paramString)
	{
		if (string == null || string.length() == 0)
			return null;
		if (jsonObject != null)
		{
			int i = 0;
			for (;;)
			{
				int j = paramString.indexOf(string, i + 1);
				if (j < 0)
				{
					break;
				}
				int k = paramString.indexOf("%", j + 1);
				String str1 = paramString.substring(j + string.length(), k);
				String str2 = "";
				Object object = null;
				ItemField itemField = info.getItemField(str1);
				if (itemField != null)
				{
					object = jsonObject.get(str1);
					if (object != null)
					{
						int n = itemField.getAccuracy();
						str2 = StringHelperUtil.objectToString(object, n);
					}
				}
				if (object == null)
				{
					str2 = "";
				}
				String str3 = DataBaseUtils.getLastCompareCode(paramString, j);
				str2 = DataBaseUtils.convertSQLQueryTransferCode(str2, str3);
				paramString = paramString.substring(0, j) + str2 + paramString.substring(k + 1);
				i += str2.length();
			}
		}

		return paramString;
	}

	public static String parseObjValueInExpression(USCObject uscObject, String paramString)
	{

		if (uscObject == null)
			return paramString;
		if (paramString == null || paramString.length() == 0)
			return null;
		paramString = parseObjValueInExpression(uscObject, "%OBJECT.", paramString);
		return paramString;

	}

	private static String parseObjValueInExpression(USCObject uscObject, String string, String paramString)
	{
		ItemInfo info = MateFactory.getItemInfo(uscObject.getItemNo());
		if (string == null || string.length() == 0)
			return null;
		if (uscObject != null)
		{
			int i = 0;
			for (;;)
			{
				int j = paramString.indexOf(string, i + 1);
				if (j < 0)
				{
					break;
				}
				int k = paramString.indexOf("%", j + 1);
				String str1 = paramString.substring(j + string.length(), k);
				String str2 = "";
				Object object = null;
				ItemField itemField = info.getItemField(str1);
				if (itemField != null)
				{
					object = uscObject.getFieldValue(str1);
					if (object != null)
					{
						int n = itemField.getAccuracy();
						str2 = StringHelperUtil.objectToString(object, n);
					}
				}
				if (object == null)
				{
					str2 = "";
				}
				String str3 = DataBaseUtils.getLastCompareCode(paramString, j);
				str2 = DataBaseUtils.convertSQLQueryTransferCode(str2, str3);
				paramString = paramString.substring(0, j) + str2 + paramString.substring(k + 1);
				i += str2.length();
			}
		}

		return paramString;
	}
}
