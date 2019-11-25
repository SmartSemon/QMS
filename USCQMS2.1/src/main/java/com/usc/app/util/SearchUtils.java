package com.usc.app.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.usc.server.jdbc.DBUtil;
import com.usc.server.md.ItemField;
import com.usc.server.md.ItemInfo;
import com.usc.server.util.LoggerFactory;
import com.usc.util.ObjectHelperUtils;

public class SearchUtils
{
	private static final String ORDER = "order";
	private static final String BY = "by";

	public static List search(ItemInfo itemInfo, String queryWord, int page) throws Exception
	{
		if (!hasQueryFields(itemInfo))
		{
			return null;
		}
		List dataList = null;
		if (!ObjectHelperUtils.isEmpty(queryWord))
		{
			String conString = "del=0 AND " + DBUtil.getItemQueryFieldsSql(itemInfo, queryWord);
			dataList = DBUtil.getSQLResultByConditionLimit(itemInfo.getItemNo(), conString, page);
		} else
		{
			dataList = DBUtil.getSQLResultByConditionLimit(itemInfo, "del=0", page);
		}
		return dataList;

	}

	public static List searchByCondition(ItemInfo itemInfo, String queryWord, String condition, int page)
			throws Exception
	{
		List dataList = null;
		if (!ObjectHelperUtils.isEmpty(queryWord))
		{
			String conString = DBUtil.getItemQueryFieldsSql(itemInfo, queryWord);
			if (ObjectHelperUtils.isEmpty(conString))
			{
				return null;
			}
			if (!ObjectHelperUtils.isEmpty(condition))
			{
				condition = getOrderByCondition(condition, conString);
			}
		}
		dataList = DBUtil.getSQLResultByConditionLimit(itemInfo, condition, page);
		return dataList;

	}

	public static List searchByCondition(ItemInfo itemInfo, String queryWord, String condition, Object[] objects,
			int[] types, int page) throws Exception
	{
		List dataList = null;
		if (!ObjectHelperUtils.isEmpty(queryWord))
		{
			String conString = DBUtil.getItemQueryFieldsSql(itemInfo, queryWord);
			if (ObjectHelperUtils.isEmpty(conString))
			{
				return null;
			}

			if (!ObjectHelperUtils.isEmpty(condition))
			{
				condition = getOrderByCondition(condition, conString);
			}
		}
		dataList = DBUtil.getSQLResultByConditionLimit(itemInfo, condition, objects, types, page);
		return dataList;

	}

	public static boolean hasQueryFields(ItemInfo itemInfo)
	{
		List<ItemField> fields = itemInfo.getQueryFieldList();
		if (ObjectHelperUtils.isEmpty(fields))
		{
			LoggerFactory.logError("Business objects have no query fields");
			return false;
		}
		return true;
	}

	public static Map<String, Object> getAddRelationPageDataCondition(List<Map> rData)
	{
		Object[] objects = null;
		int[] types = null;
		StringBuffer cond = new StringBuffer();
		if (!ObjectHelperUtils.isEmpty(rData))
		{
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			cond.append("del =0 AND id NOT IN(");
			int size = rData.size();
			objects = new Object[size];
			types = new int[size];
			for (int i = 0; i < size; i++)
			{
				String v = (String) rData.get(i).get("id");
				objects[i] = v;
				types[i] = Types.VARCHAR;
				if (i > 0)
				{
					cond.append(",");
				}
				cond.append("?");
			}
			cond.append(")");
			hashMap.put("objects", objects);
			hashMap.put("types", types);
			hashMap.put("condition", cond.toString());
			return hashMap;
		} else
		{
			return null;
		}

	}

	private static String getOrderByCondition(String nodeCondition, String queryCondition)
	{
		String pSql = nodeCondition.toLowerCase();
		if (!pSql.contains(ORDER))
		{
			return nodeCondition + " AND " + queryCondition;
		}

		int podIndex = pSql.indexOf(BY) + 2;
		String pOrderfs = nodeCondition.substring(podIndex, nodeCondition.length()).trim();
		String order = " ORDER BY ";
		return nodeCondition.substring(0, pSql.indexOf(ORDER)) + " AND " + queryCondition + order + pOrderfs;
	}
}
