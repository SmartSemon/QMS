package com.usc.test.mate.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.cache.redis.RedisUtil;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.util.SystemTime;
import com.usc.server.util.uuid.USCUUID;

/**
 *
 * <p>
 * Title: ModelCreateAction
 * </p>
 *
 * <p>
 * Description: 创建模型业务对象数据
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年4月12日
 *
 */
@Component
public class MCreateAction
{
	public MCreateAction()
	{
	}

	@Autowired
	private static JdbcTemplate jdbcTemplate;

	private final static String INSERT_INTO = "insert into ";
	private final static String _L = " (";
	private final static String _VALUES = ",id,del,ctime,state,cuser) values (";
	private final static String _R = ")";

	private static List<Map> list;

	private static String name;

	private static int itemType;

	private static int islife;

	static String cuser;

	static RedisUtil redisUtil = new RedisUtil();

	public static Map createModelObj(JdbcTemplate jdbcTemplate1, String jsonString)
	{
		if (jsonString == null)
			return null;
		jdbcTemplate = jdbcTemplate1;
		JSONObject jsonObject = JSON.parseObject(jsonString);
		cuser = jsonObject.getString("userName");
		String tn = jsonObject.getString("tableName");
		Map<String, Object> data = JSONObject.parseObject(String.valueOf(jsonObject.getString("data")));
		data.put("ver", 0);
		data.put("effective", 0);
		if (data.containsKey("briefexp"))
		{
			data.put("briefexp", String.valueOf(data.get("briefexp")));
		}
		;
		String tableNo = (String) data.get("itemno");
		String tableName = (String) data.get("tablename");

		List<Map<String, Object>> oldItemList = DBUtil.queryForList("usc_model_item",
				"del=? AND (itemno=? OR tablename=?)", new Object[]
				{ 1, tableNo, tableName });
		if (oldItemList != null)
		{
			String updateItemSql = "UPDATE " + tn + " SET name='" + data.get("name") + "',islife=" + data.get("islife")
					+ ",type=" + data.get("type") + ",queryfields=null,remark='" + data.get("remark")
					+ ",del=0,mysm='M',state='F' WHERE id='" + oldItemList.get(0).get("id") + "'";
			String updateItemFieldSql = "UPDATE usc_model_field SET mysm='M',del=0,state='F' WHERE itemid='"
					+ oldItemList.get(0).get("id") + "'";
			try
			{
				String[] sqls = new String[]
				{ updateItemSql, updateItemFieldSql };
				jdbcTemplate.batchUpdate(sqls);
			} catch (Exception e)
			{
				return StandardResultTranslate.getResult(false, e.getMessage());
			}

		} else
		{
			List<Map<String, Object>> itemList = DBUtil.queryForList("usc_model_item",
					"del=? AND (itemno=? OR tablename=?)", new Object[]
					{ 0, tableNo, tableName });
			if (itemList == null)
			{
				data.put("mysm", "N");
				Object[] objects = getObject(tn, data);
				name = (String) data.get("name");
				itemType = (Integer) data.get("type");
				islife = (Integer) data.get("islife");
				BatchInsertM(data, (String) objects[0], (Object[]) objects[1]);
			} else
			{
				return StandardResultTranslate.getResult("对象已存在", false);
			}
		}

		return StandardResultTranslate.getResult(true, "ACTION_CREATE");
	}

	private static Object[] getObject(String table, Map<String, Object> data)
	{
		StringBuffer fields = new StringBuffer("");
		StringBuffer values = new StringBuffer("?,?,?,?,?");
		int len = data.keySet().size() + 5;
		Object[] objects = new Object[len];
		int i = 0;
		for (Object object : data.keySet())
		{
			if (i != 0)
			{
				fields.append(",");
			}

			fields.append((String) object);
			values.append(",?");
			objects[i] = data.get(object);
			i++;
		}
		objects[i] = USCUUID.UUID();
		objects[i + 1] = 0;
		objects[i + 2] = SystemTime.getTimestamp();
		objects[i + 3] = "C";
		objects[i + 4] = cuser;
		data.put("id", objects[len - 5]);
		data.put("del", objects[len - 4]);
		data.put("ctime", objects[len - 3]);
		data.put("state", objects[len - 2]);
		data.put("cuser", objects[len - 1]);

		String insertSql = INSERT_INTO + table + _L + fields.toString() + _VALUES + values + _R;
		return new Object[]
		{ insertSql, objects, data };
	}

	@Transactional
	public static Map<String, Object> BatchInsertM(Map<String, Object> map, String insertSql, Object... objects)
	{
		Map<String, Object> newData = map;
		if (DBUtil.insertOrUpdate(insertSql, objects))
		{
			String itemid = (String) newData.get("id");
			createModeldefaultvFields(itemid);
			createModeldefaultvGrids(itemid);
			createModeldefaultvPropertice(itemid);
			createModeldefaultvMenus(itemid);
			return StandardResultTranslate.getResult("Action_Create", newData);
		} else
		{
			return StandardResultTranslate.getResult(false, "Action_Create");
		}

	}

	private static void createModeldefaultvGrids(String itemid)
	{
		Vector<String> idV = createData(MDefaultValues.getDefaultGrid(itemid), "usc_model_grid");
		if (idV != null)
		{
			createModeldefaultvGridField(itemid, idV.get(0));
		}

	}

	private static void createModeldefaultvGridField(String itemid, String rootid)
	{
		createData(MDefaultValues.getDefaultGridField(itemType, itemid, rootid), "usc_model_grid_field");

	}

	private static void createModeldefaultvPropertice(String itemid)
	{
		Vector<String> idV = createData(MDefaultValues.getDefaultProperty(itemid), "usc_model_property");
		if (idV != null)
		{
			createData(MDefaultValues.getDefaultPropertyField(itemType, itemid, idV.get(0)),
					"usc_model_property_field");
		}

	}

	private static void createModeldefaultvMenus(String itemid)
	{

		String table = "usc_model_menu";
		String condition = "del=? AND state=? AND implclass IN(?,?)";
		Object[] objects = new Object[]
		{ 0, "F", "com.unismartcore.app.action.DeleteUSCObjectAction",
				"com.unismartcore.app.action.BatchModifyAction" };
		List<Map<String, Object>> list = DBUtil.queryForList(table, condition, objects);
		if (list != null)
		{
			createData(MDefaultValues.getDefaultMenu(list, itemid), "usc_model_itemmenu");
		}
	}

	private static Vector<String> createData(List<Map<String, Object>> list2, String tableName)
	{
		List<Object[]> list = new ArrayList<Object[]>();
		Vector<String> ids = new Vector<String>();
		Object[] objects = null;
		for (Map map : list2)
		{
			objects = getObject(tableName, map);
			Object[] objs = (Object[]) objects[1];
			list.add(objs);
			Map<String, Object> newData = (Map<String, Object>) objects[2];
			ids.add((String) newData.get("id"));
		}

		String insertSql = (String) objects[0];

		jdbcTemplate.batchUpdate(insertSql, list);
		return ids;
	}

	private static void createModeldefaultvFields(String itemid)
	{
		createData(MDefaultValues.getDefaultField(itemType, itemid), "usc_model_field");

	}

}
