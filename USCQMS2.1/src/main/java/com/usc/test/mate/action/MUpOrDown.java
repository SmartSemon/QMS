package com.usc.test.mate.action;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.util.LoggerFactory;

public class MUpOrDown
{

	public static Map<String, Object> move(String queryParam)
	{
		try
		{
			JSONObject jsonObject = JSONObject.parseObject(queryParam);
			String tableName = jsonObject.getString("tableName");
			List<Map> list = JSONArray.parseArray(jsonObject.get("hData").toString(), Map.class);
			if (list == null || list.size() < 2)
			{
				return StandardResultTranslate.failedOperation();
			}
			int size = list.size();
			Map<String, Object> m1 = list.get(0);
			Map<String, Object> m2 = list.get(1);
			int n1 = (Integer) m1.get("sort");
			int n2 = (Integer) m2.get("sort");
			List<Object[]> objList = new Vector<Object[]>();
			if (n1 > n2)
			{
				doUp(list, size, n2, objList);
			} else
			{
				doDown(list, size, objList);
			}
			String sql = "UPDATE " + tableName + " SET sort=? WHERE id=?";
			if (DBUtil.BathInsertOrUpdate(sql, objList))
			{
				return StandardResultTranslate.successfulOperation();
			}
		} catch (Exception e)
		{
			LoggerFactory.logError(StandardResultTranslate.translate("ACtion_Move_2"), e);
		}
		return StandardResultTranslate.failedOperation();

	}

	public static Map<String, Object> menuMove(String queryParam)
	{
		try
		{
			JSONObject jsonObject = JSONObject.parseObject(queryParam);
			String tableName = jsonObject.getString("tableName");
			List<Map> list = JSONArray.parseArray(jsonObject.get("hData").toString(), Map.class);
			if (list == null || list.size() < 2)
			{
				return StandardResultTranslate.failedOperation();
			}
			Map<String, Object> m1 = list.get(0);
			Map<String, Object> m2 = list.get(1);
			if (!m1.get("pid").equals(m2.get("pid")))
			{
				return StandardResultTranslate.failedOperation();
			}
			int n1 = (Integer) m1.get("sort");
			int n2 = (Integer) m2.get("sort");
			List<Object[]> objList = new Vector<Object[]>();
			objList.add(new Object[]
			{ n2, m1.get("id") });
			objList.add(new Object[]
			{ n1, m2.get("id") });
			String sql = "UPDATE " + tableName + " SET sort=? WHERE id=?";
			if (DBUtil.BathInsertOrUpdate(sql, objList))
			{
				return StandardResultTranslate.successfulOperation();
			}
		} catch (Exception e)
		{
			LoggerFactory.logError(StandardResultTranslate.translate("ACtion_Move_2"), e);
		}
		return StandardResultTranslate.failedOperation();

	}

	private static void doUp(List<Map> list, int size, int n2, List<Object[]> objList)
	{
		for (int i = 0; i < size; i++)
		{
			int sort = n2 + i;
			Map<String, Object> map = list.get(i);
			addObj(objList, map, sort);
		}
	}

	private static void doDown(List<Map> list, int size, List<Object[]> objList)
	{
		Map<String, Object> m1 = list.get(size - 1);
		int n1 = (Integer) m1.get("sort");
		for (int i = 0; i < size; i++)
		{
			int sort = n1 + i;
			Map<String, Object> map = list.get(i);
			addObj(objList, map, sort);
		}
	}

	private static void addObj(List<Object[]> objList, Map<String, Object> map, int sort)
	{
		Object[] objects = new Object[2];
		String id = (String) map.get("id");
		objects[0] = sort;
		objects[1] = id;
		objList.add(objects);
	}

}
