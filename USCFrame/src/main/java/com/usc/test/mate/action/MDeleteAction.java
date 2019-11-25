package com.usc.test.mate.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usc.server.util.SystemTime;

/**
 *
 * <p>
 * Title: ModelDeleteAction
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年4月15日
 *
 */
@RestController
public class MDeleteAction
{

	public static Boolean delete(JdbcTemplate jdbcTemplate, JSONObject jsonObject)
	{
		if (jsonObject == null)
			return false;
		Object table = jsonObject.get("tableName");
		String state = String.valueOf(jsonObject.get("state")).replace("null", "C");
		String sql = "C".equals(state) ? getSqlD(table) : getSqlU(table);
		if (sql == null)
			return false;
		JSONArray jsonArray = JSONArray.parseArray(String.valueOf(jsonObject.get("data")));
		List<Map> maps = JSONArray.parseArray(jsonArray.toJSONString(), Map.class);
		if (maps == null)
			return false;
		if ("C".equals(state))
		{
			for (Map dto : maps)
			{
				physicsDeleteItemRelationInfo(jdbcTemplate, table.toString(), dto);
			}

		}
		if ("U".equals(state))
		{
			for (Map dto : maps)
			{
				physicsDeleteItemRelationInfo(jdbcTemplate, table.toString(), dto);
			}

		}
		if ("F".equals(state))
		{
			List<Object[]> objects = new ArrayList<Object[]>(maps.size());
			for (Map dto : maps)
			{
				Object[] objs = new Object[]
				{ "D", 1, "HS", dto.get("duser"), SystemTime.getTimestamp(), dto.get("id") };
				objects.add(objs);
			}
			jdbcTemplate.batchUpdate(sql, objects);
		}

		return Boolean.TRUE;
	}

	private static void physicsDeleteItemRelationInfo(JdbcTemplate jdbcTemplate, String tableName, Map dto)
	{
		String pdSql = "DELETE FROM " + tableName + " WHERE id='" + (String) dto.get("id") + "';";
		jdbcTemplate.execute(pdSql);
		if (tableName.equals("usc_model_itemmenu"))
		{
			String cMSql = "DELETE FROM " + tableName + " WHERE pid='" + (String) dto.get("id") + "';";
			jdbcTemplate.execute(cMSql);
		}
	}

	private static String getSqlU(Object table)
	{
		return table != null ? "UPDATE " + table + " SET mysm=?,del=?,state=?,duser=?,dtime=? WHERE id=?" : null;
	}

	private static String getSqlD(Object table)
	{
		return table != null ? "DELETE FROM " + table + " WHERE id=?" : null;
	}

}
