package com.usc.test.mate.action.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usc.app.action.utils.ActionMessage;
import com.usc.cache.redis.RedisUtil;
import com.usc.test.mate.action.MCreateAction;
import com.usc.test.mate.action.MUpdateAction;
import com.usc.test.mate.action.service.ModelItemServer;

@Service("modelItemServer")
public class ModelItemServerImpl implements ModelItemServer
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	RedisUtil redis = new RedisUtil();

	@Override
	public Object createItem(String params)
	{

		return MCreateAction.createModelObj(jdbcTemplate, params);
	}

	@Override
	public Object updateItem(String params)
	{
		try
		{
			return MUpdateAction.update(params);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object deleteItem(String params)
	{
		JSONObject jsonObject = JSONObject.parseObject(params);
		String user = jsonObject.getString("userName");
		JSONObject map = new JSONObject();
		if (redis.hasKey("OPENMODEL"))
		{

			String muser = (String) redis.get("OPENMODEL");
			if (!user.equals(muser))
			{
				map.put("flag", false);
				map.put("info", "用户：" + muser + " 正在建模，您无权操作建模数据");
				return map;
			}
		} else
		{
			map.put("flag", false);
			map.put("info", "请先开启建模工作权限");
			return map;
		}
		String tableName = jsonObject.getString("tableName");
		List<JSONObject> jsonArray = JSONArray.parseArray(jsonObject.getString("data"), JSONObject.class);
		for (JSONObject object : jsonArray)
		{
			String id = object.getString("id");
			String state = object.getString("state");
			if ("C".equals(state))
			{
				deleteC(tableName, id);
			}
			if ("U".equals(state))
			{
				deleteU(tableName, id, object.getIntValue("ver"));
			}
			if ("F".equals(state))
			{
				deleteF(tableName, id, user);
			}
		}
		return null;
	}

	@Override
	public Object recoveryItem(String params)
	{
		JSONObject jsonObject = JSONObject.parseObject(params);
		String table = jsonObject.getString("tableName");
		List<JSONObject> jsonArray = JSONArray.parseArray(jsonObject.getString("data"), JSONObject.class);
		for (JSONObject object : jsonArray)
		{
			String id = object.getString("id");
			Integer eff = object.getInteger("effective");
			if (eff == 1)
			{
				jdbcTemplate.batchUpdate("UPDATE " + table + " SET state='F' WHERE id='" + id + "'");
			} else
			{
				recovery(table, id);
			}

		}

		return new ActionMessage(true, "D", "恢复成功", null, null);
	}

	private void deleteC(String tableName, String id)
	{
		String[] sqls = null;
		if (tableName.equals("usc_model_item"))
		{
			sqls = new String[]
			{ "DELETE FROM usc_model_item WHERE id='" + id + "'",
					"DELETE FROM usc_model_field WHERE itemid='" + id + "'",
					"DELETE FROM usc_model_itemmenu WHERE itemid='" + id + "'",
					"DELETE FROM usc_model_grid WHERE itemid='" + id + "'",
					"DELETE FROM usc_model_grid_field WHERE itemid='" + id + "'",
					"DELETE FROM usc_model_property WHERE itemid='" + id + "'",
					"DELETE FROM usc_model_property_field WHERE itemid='" + id + "'",
					"DELETE FROM usc_model_relationpage WHERE itemid='" + id + "'",
					"DELETE FROM usc_model_relationpage_sign WHERE itemid='" + id + "'" };
		}
		if (tableName.equals("usc_model_relationship") || tableName.equals("usc_model_queryview"))
		{
			sqls = new String[]
			{ "DELETE FROM " + tableName + " WHERE id='" + id + "'",
					"DELETE FROM usc_model_itemmenu WHERE itemid='" + id + "'" };
		}
		if (tableName.equals("usc_model_classview"))
		{
			sqls = new String[]
			{ "DELETE FROM usc_model_classview WHERE id='" + id + "'",
					"DELETE FROM usc_model_classview_node WHERE itemid='" + id + "'",
					"DELETE FROM usc_model_itemmenu WHERE itemid='" + id + "'" };
		}
		if (sqls != null)
		{
			jdbcTemplate.batchUpdate(sqls);
		}

	}

	private void deleteU(String table, String id, int ver)
	{
		Integer v = ver - 1;
		if (table.equals("usc_model_item"))
		{
			jdbcTemplate.batchUpdate("UPDATE " + table + " SET state='F',del=0,effective=1 WHERE del=0 AND ver=" + v
					+ " AND tablename=(SELECT T.tablename FROM (SELECT tablename FROM " + table + " WHERE id='" + id
					+ "') T)");
		} else
		{
			jdbcTemplate.batchUpdate("UPDATE " + table + " SET state='F',del=0,effective=1 WHERE del=0 AND ver=" + v
					+ " AND no=(SELECT T.no FROM (SELECT no FROM " + table + " WHERE id='" + id + "') T)");
		}
		deleteC(table, id);
	}

	private void deleteF(String tableName, String itemID, String user)
	{
		String[] sqls = new String[]
		{ "UPDATE " + tableName + " SET state='HS', duser='" + user + "',dtime=(select now()) WHERE id='" + itemID
				+ "'" };
		jdbcTemplate.batchUpdate(sqls);
	}

	private void recovery(String tableName, String id)
	{
		String[] sqls = null;
		if (tableName.equals("usc_model_item"))
		{
			sqls = new String[]
			{ "UPDATE usc_model_item SET state='F', del=0, effective=-2 WHERE id='" + id + "'",
					"UPDATE usc_model_itemmenu SET state='F', del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_grid SET state='F', del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_grid_field SET state='F', del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_property SET state='F', del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_property_field SET state='F', del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_relationpage SET state='F', del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_relationpage_sign SET state='F', del=0 WHERE itemid='" + id + "'" };
		}
		if (tableName.equals("usc_model_relationship") || tableName.equals("usc_model_queryview"))
		{
			sqls = new String[]
			{ "UPDATE " + tableName + " SET state='F', del=0, effective=-2 WHERE id='" + id + "'",
					"UPDATE usc_model_itemmenu SET state='F', del=0 WHERE itemid='" + id + "'" };
		}
		if (tableName.equals("usc_model_classview"))
		{
			sqls = new String[]
			{ "UPDATE " + tableName + " SET state='F', del=0, effective=-2 WHERE id='" + id + "'",
					"UPDATE usc_model_node SET state='F', del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_itemmenu SET state='F', del=0 WHERE itemid='" + id + "'" };
		}

		jdbcTemplate.batchUpdate(sqls);
	}

}
