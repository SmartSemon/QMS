package com.usc.test.mate.action;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.server.jdbc.DBUtil;

public class MRelationQueryAction
{
	public static Map<String, Object> query(String queryParam)
	{

		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		String table = jsonObject.getString("tableName");
		String con = jsonObject.getString("condition");
		String[] cond = getCondition(table, con);
		Object[] objects = null;
		if (cond[1] == null)
		{
			objects = new Object[]
			{ 0, "F", "C", "U" };
		} else
		{
			objects = new Object[]
			{ 0, "F", "C", "U", cond[1] };
		}
		List<Map<String, Object>> dataList = DBUtil.queryForList(table, cond[0], objects);
		return StandardResultTranslate.getQueryResult(true, "Action_Query", dataList);
	}

	private static String[] getCondition(String table, String con)
	{
		StringBuffer condition = new StringBuffer("del=? AND state IN(? ,? ,? )");
		String v = null;
		if (table.equals("usc_model_field") || table.equals("usc_model_grid") || table.equals("usc_model_itemmenu")
				|| table.equals("usc_model_property") || table.equals("usc_model_relationpage")
				|| table.equals("usc_model_classview_node"))
		{
			if (con != null)
			{
				v = con.substring(con.indexOf("'") + 1, con.length() - 1);
				condition.append(" AND itemid=?");
			}
		} else
		{
			if (con != null)
			{
				v = con.substring(con.indexOf("'") + 1, con.length() - 1);
				condition.append(" AND rootid=?");
			}
		}

		return new String[]
		{ condition.toString() + " ORDER BY sort", v };
	}
}
