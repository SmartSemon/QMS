package com.usc.app.view.search;

import com.usc.obj.api.impl.ApplicationContext;

public class ClassViewNodeReplaceSql
{
	private static final String PARENT = "PARENT.";
	private static final String split = "#";
	private static final String sign = "%";
	private static final String CLASSVIEWTREENODEVALUE = "CLASSVIEWTREENODEVALUE";

	public static String replaceCNodeDataSql(ApplicationContext context, String cNodeDataSql, String treenodedata,
			String pNodeID)
	{
		if (cNodeDataSql.contains(CLASSVIEWTREENODEVALUE + sign))
		{
			String value = sign + CLASSVIEWTREENODEVALUE + sign;
			if (cNodeDataSql.contains(value))
			{
				cNodeDataSql = cNodeDataSql.replace(value, treenodedata);
			}
			int i = 1;
			while (cNodeDataSql.contains(CLASSVIEWTREENODEVALUE + sign))
			{
				if (i == 20)
				{
					return cNodeDataSql;
				}
				int index = cNodeDataSql.indexOf(CLASSVIEWTREENODEVALUE + sign);
				int n = index + (CLASSVIEWTREENODEVALUE + sign).length();
				while (index > 6)
				{
					String b = cNodeDataSql.substring(index - 2, index - 1);
					if (b.equals(sign))
					{
						String nodeValue = cNodeDataSql.substring(index - 2, n);
						String str = replaceParentNodeData(nodeValue, pNodeID);
						cNodeDataSql = cNodeDataSql.replace(nodeValue, str);
						break;
					}
					index--;

				}
				i++;
			}
		}

		return cNodeDataSql.replace("%SYSTEMUSER%", context.getUserName());
	}

	private static String replaceParentNodeData(String nodeValue, String pNodeID)
	{
		int n = 0;
		while (nodeValue.contains(PARENT))
		{
			n++;
			nodeValue = nodeValue.substring(nodeValue.indexOf(PARENT) + 7);
		}
		return replaceParentNodeData(n, pNodeID);
	}

	private static String replaceParentNodeData(int n, String pNodeID)
	{
		String[] pNodeIDs = pNodeID.split(split);
		int len = pNodeIDs.length;
		if (n > len || n < 1)
		{
			return "null";
		}
		return pNodeIDs[len - n];
	}
}
