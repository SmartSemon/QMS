package com.usc.test.mate.resource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.action.mate.MateFactory;
import com.usc.cache.redis.RedisUtil;
import com.usc.server.init.InitJurisdictionData;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ModelClassView;
import com.usc.server.md.ModelQueryView;
import com.usc.server.md.ModelRelationShip;
import com.usc.server.md.USCModelMate;
import com.usc.server.md.mapper.ItemRowMapper;
import com.usc.server.md.mapper.ModelClassViewRowMapper;
import com.usc.server.md.mapper.ModelQueryViewRowMapper;
import com.usc.server.md.mapper.RelationShipRowMapper;
import com.usc.server.util.LoggerFactory;
import com.usc.test.mate.action.service.ModelServer;
import com.usc.util.ObjectHelperUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/modelSynchronous", produces = "application/json;charset=UTF-8")
@Slf4j
public class SysModelSynchronousDBServiceResource
{
	RedisUtil redis = new RedisUtil();
	@Autowired
	private ModelServer modelServer;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String DATETIME = "DATETIME";
	private final String VARCHAR = "VARCHAR";
	private final String INT = "INT";
	private final String DOUBLE = "DOUBLE";
	private final String NUMBER = "NUMBER";
	private final String FLOAT = "FLOAT";
	private final String BOOLEAN = "BOOLEAN";
	private final String LONGTEXT = "LONGTEXT";

	/**
	 * <p>
	 * 新建model_item 对象数据同步模型数据到数据库
	 *
	 * @param queryParam
	 * @return
	 */

	@Transactional
	@PostMapping("/all")
	public Object synchronous(@RequestBody String params)
	{
		String user = JSONObject.parseObject(params).getString("userName");
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
		return synchronousDBRedis();
	}

	@Transactional
	private synchronized Object synchronousDBRedis()
	{
		LoggerFactory.logInfo("----------开始同步数据库相关数据----------");
		JSONObject map = new JSONObject();

		synchronousItemC();
		synchronousItemU();
		synchronousItemF();
		synchronousItemHS();
		LoggerFactory.logInfo("----------初始化建模相关数据完成----------");
		LoggerFactory.logInfo("----------开始初始化模块权限相关数据----------");
		InitJurisdictionData.init(jdbcTemplate);
		LoggerFactory.logInfo("----------初始化模块权限相关数据完成，同步工作完成----------");
		map.put("flag", true);
		map.put("info", "同步完成");
		return map;

	}

	private void synchronousItemHS()
	{
		List<Map<String, Object>> itemsM = jdbcTemplate
				.queryForList("SELECT * FROM usc_model_item WHERE del=0 AND state='HS' AND effective=1");
		if (!ObjectHelperUtils.isEmpty(itemsM))
		{
			for (Map<String, Object> map : itemsM)
			{
				SynchItemHS("usc_model_item", map);
			}
		}
	}

	/**
	 * <P>
	 * 同步已生效的业务对象修改后的所有信息（业务对象，字段，菜单，属性页，表格）
	 */
	private void synchronousItemU()
	{
		String[] modelTables = getModelTables();
		for (String table : modelTables)
		{
			List<Map<String, Object>> itemsM = jdbcTemplate
					.queryForList("SELECT * FROM " + table + " WHERE del=0 AND state='U'");
			if (!ObjectHelperUtils.isEmpty(itemsM))
			{
				for (Map<String, Object> itemMap : itemsM)
				{
					SynchItemU(table, itemMap);
				}
			}

		}
	}

	/**
	 * <P>
	 * 同步已生效的业务对象新建的关联信息（字段，菜单，属性页，表格）
	 */
	private void synchronousItemF()
	{
		LoggerFactory.logInfo("----------开始同步新增字段对线数据库相关数据----------");
		String[] modelTables = getModelTables();
		for (String table : modelTables)
		{
			List<Map<String, Object>> maps = jdbcTemplate
					.queryForList("SELECT * FROM " + table + " WHERE del=0 AND state='F' AND effective<>1");
			if (!ObjectHelperUtils.isEmpty(maps))
			{
				for (Map<String, Object> map : maps)
				{
					SynchItemF(table, map);
				}
			}
		}
	}

	/**
	 * <P>
	 * 同步新建未生效的业务对象新建的所有数据（业务对象，字段，菜单，属性页，表格）
	 */

	@Transactional(rollbackFor = Exception.class)
	private void synchronousItemC()
	{
		String[] modelTables = getModelTables();
		for (String table : modelTables)
		{
			List<Map<String, Object>> itemC = jdbcTemplate
					.queryForList("SELECT * FROM " + table + " WHERE del=0 AND state='C'");
			if (itemC != null && itemC.size() > 0)
			{
				for (Map<String, Object> itemMap : itemC)
				{
					SynchItemC(table, itemMap);
				}
			}
		}

	}

	@PostMapping("/single")
	public Object singleSynchronization(@RequestBody String param)
	{
		JSONObject jsonObject = JSONObject.parseObject(param);
		String userName = jsonObject.getString("userName");
		Map<String, Object> result = new HashMap<String, Object>();
		if (!modelServer.isModelingUser(userName))
		{
			result.put("flag", false);
			result.put("info", "未开启建模，禁止操作同步功能");
			return result;
		}
		boolean b = false;

		JSONObject object = jsonObject.getJSONObject("data");
		String state = object.getString("state");
		String table = jsonObject.getString("tableName");
		if ("U".equals(state))
		{
			b = SynchItemU(table, object);
		}
		if ("C".equals(state))
		{
			b = SynchItemC(table, object);
		}
		if ("HS".equals(state))
		{
			b = SynchItemHS(table, object);

		}
		if ("F".equals(state))
		{
			if (0 == object.getIntValue("effective"))
			{
				b = SynchItemF(table, object);
			}
		}

		if (b)
		{
			result.put("info", "单条建模同步成功");
		} else
		{
			result.put("info", "单条建模同步失败");
		}
		result.put("flag", b);

		return result;

	}

	private boolean SynchItemC(String table, Map<String, Object> object)
	{
		boolean b = false;
		String id = (String) object.get("id");
		if (table.equals("usc_model_item"))
		{
			String itemNo = (String) object.get("itemno");
//			String name = (String) object.get("name");
			String tableName = (String) object.get("tablename");

			String createTableSql = getCreateTableSql(object);
			if (createTableSql == null)
			{
				log.error("同步对象>>> " + itemNo + " 失败！原因：未正常创建表对象",
						new SQLException("failed: tableName >>>" + tableName));
			} else
			{
				try
				{
					jdbcTemplate.batchUpdate(createTableSql);
					execute("state='F',mysm=null", id, null);
					return initModel(table, id);
				} catch (Exception e)
				{

				}

			}
		}

		if (table.equals("usc_model_classview"))
		{
			int[] is = jdbcTemplate.batchUpdate(new String[]
			{ "UPDATE " + table + " SET state='F' ,effective=1 WHERE id='" + id + "'",
					"UPDATE usc_model_itemmenu SET state='F' WHERE del=0 AND itemid='" + id + "'",
					"UPDATE usc_model_classview_node SET state='F'  WHERE del=0 AND itemid='" + id + "'" });
			b = 1 == is[0];

			return b ? initModel(table, id) : false;
		}

		int[] is = jdbcTemplate.batchUpdate(new String[]
		{ "UPDATE " + table + " SET state='F' ,effective=1 WHERE id='" + id + "'",
				"UPDATE usc_model_itemmenu SET state='F' WHERE del=0 AND itemid='" + id + "'" });
		b = 1 == is[0];

		return b ? initModel(table, id) : false;
	}

	private boolean SynchItemU(String table, Map<String, Object> object)
	{
		List<String> sqls = new Vector<String>();
		boolean b = false;
		String id = (String) object.get("id");
		Integer ver = (Integer) object.get("ver");
		if (table.equals("usc_model_item"))
		{
//			String itemNo = (String) object.get("itemno");
			String name = (String) object.get("name");
			String tableName = (String) object.get("tablename");
			sqls.add("ALTER TABLE " + tableName + " COMMENT '" + name + "'");
			List<Map<String, Object>> fieldC = jdbcTemplate.queryForList("SELECT * FROM usc_model_field WHERE "
					+ "del=0 AND state='C' AND itemid='" + id + "' ORDER BY SORT");
			if (!CollectionUtils.isEmpty(fieldC))
			{
				b = addFields(sqls, tableName, fieldC);
			}

			List<Map<String, Object>> fieldM = jdbcTemplate.queryForList("SELECT * FROM usc_model_field WHERE "
					+ "del=0 AND state='U' AND mysm='M' AND itemid='" + id + "' ORDER BY SORT");
			if (!CollectionUtils.isEmpty(fieldM))
			{
				b = modifiyFields(sqls, tableName, fieldM);
			}

			sqls.add("UPDATE usc_model_item SET state='HS',effective=0,del=0 WHERE del=0 AND ver<>" + ver
					+ " AND tablename='" + tableName + "'");
			jdbcTemplate.batchUpdate(sqls.toArray(new String[sqls.size()]));
			execute("state='F',mysm=null", id, null);
			return b ? initModel(table, id) : false;
		}

		String oldVerID = "(SELECT T.id FROM (SELECT id FROM usc_model_relationship WHERE del=0 AND ver=" + (ver - 1)
				+ " AND no='" + object.get("no") + "') T)";
		if (table.equals("usc_model_classview"))
		{
			int[] is = jdbcTemplate.batchUpdate(new String[]
			{ "UPDATE " + table + " SET state='F' ,effective=1 WHERE id='" + id + "'",
					"UPDATE usc_model_itemmenu SET state='F' WHERE del=0 AND itemid='" + id + "'",
					"UPDATE usc_model_classview_node SET state='F'  WHERE del=0 AND itemid='" + id + "'",
					"UPDATE " + table + " SET state='HS',effective=0 WHERE id=" + oldVerID,
					"UPDATE usc_model_itemmenu SET state='HS' WHERE del=0 AND itemid=" + oldVerID,
					"UPDATE usc_model_classview_node SET state='HS'  WHERE del=0 AND itemid=" + oldVerID });
			b = 1 == is[0];
			return b ? initModel(table, id) : false;
		}

		int[] is = jdbcTemplate.batchUpdate(new String[]
		{ "UPDATE " + table + " SET state='F' ,effective=1 WHERE id='" + id + "'",
				"UPDATE usc_model_itemmenu SET state='F' WHERE del=0 AND itemid='" + id + "'",
				"UPDATE " + table + " SET state='HS',effective=0 WHERE id=" + oldVerID,
				"UPDATE usc_model_itemmenu SET state='HS' WHERE del=0 AND itemid=" + oldVerID });
		b = 1 == is[0];

		return b ? initModel(table, id) : false;
	}

	private boolean SynchItemHS(String table, Map<String, Object> object)
	{
		boolean b = false;
		String id = (String) object.get("id");

		Integer eff = (Integer) object.get("effective");
		if (1 == eff)
		{
			if (table.equals("usc_model_item"))
			{
				String itemNo = (String) object.get("itemno");
				String tableName = (String) object.get("tablename");
				int[] is = jdbcTemplate.batchUpdate(new String[]
				{ "UPDATE usc_model_item SET state='HS' ,del=0,effective=0 WHERE id='" + id + "'",
						"UPDATE usc_model_itemmenu SET state='HS' ,del=0 WHERE itemid='" + id + "'",
						"UPDATE usc_model_field SET state='HS' ,del=0 WHERE itemid='" + id + "'",
						"UPDATE usc_model_grid SET state='HS' ,del=0 WHERE itemid='" + id + "'",
						"UPDATE usc_model_grid_field SET state='HS' ,del=0 WHERE itemid='" + id + "'",
						"UPDATE usc_model_property SET state='HS' ,del=0 WHERE itemid='" + id + "'",
						"UPDATE usc_model_property_field SET state='HS' ,del=0 WHERE itemid='" + id + "'",
						"UPDATE usc_model_relationpage SET state='HS' ,del=0 WHERE itemid='" + id + "'",
						"UPDATE usc_model_relationpage_sign SET state='HS' ,del=0 WHERE itemid='" + id + "'" });
				if (1 == is[0])
				{
					ItemInfo itemInfo = jdbcTemplate
							.queryForObject("SELECT * FROM usc_model_item WHERE id='" + id + "'", new ItemRowMapper());
					if (itemInfo != null)
					{
						redis.hdel("MODEL_ITEMDATA", itemNo);
						redis.hdel("MODEL_ITEMDATABYTABLE", tableName);
					}
				}
			}
			if (table.equals("usc_model_classview"))
			{
				int[] is = jdbcTemplate.batchUpdate(new String[]
				{ "UPDATE " + table + " SET state='HS' ,effective=0 WHERE id='" + id + "'",
						"UPDATE usc_model_itemmenu SET state='HS' WHERE del=0 AND itemid='" + id + "'",
						"UPDATE usc_model_classview_node SET state='HS'  WHERE del=0 AND itemid='" + id + "'", });
				b = 1 == is[0];
				if (b)
				{
					redis.hdel("MODEL_QUERYVIEWDATA", object.get("no"));
				}
				return b;
			}

			int[] is = jdbcTemplate.batchUpdate(new String[]
			{ "UPDATE " + table + " SET state='HS' ,effective=0 WHERE id='" + id + "'",
					"UPDATE usc_model_itemmenu SET state='HS' WHERE del=0 AND itemid='" + id + "'" });
			b = 1 == is[0];
			if (table.equals("usc_model_relationship"))
			{

				if (b)
				{
					redis.hdel("MODEL_RELATIONSHIPDATA", object.get("no"));
				}
			}
			if (table.equals("usc_model_queryview"))
			{
				if (b)
				{
					redis.hdel("MODEL_QUERYVIEWDATA", object.get("no"));
				}
			}
		}
		return false;

	}

	private boolean SynchItemF(String table, Map<String, Object> object)
	{
		boolean b = false;
		String id = (String) object.get("id");

		if ("usc_model_item".equals(table))
		{
			int[] is = jdbcTemplate.batchUpdate(new String[]
			{ "UPDATE usc_model_item SET state='F' ,del=0,effective=1 WHERE id='" + id + "'",
					"UPDATE usc_model_itemmenu SET state='F' ,del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_field SET state='F' ,del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_grid SET state='F' ,del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_grid_field SET state='F' ,del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_property SET state='F' ,del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_property_field SET state='F' ,del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_relationpage SET state='F' ,del=0 WHERE itemid='" + id + "'",
					"UPDATE usc_model_relationpage_sign SET state='F' ,del=0 WHERE itemid='" + id + "'" });
			b = 1 == is[0];
			return b ? initModel(table, id) : false;
		}

		if (table.equals("usc_model_classview"))
		{
			int[] is = jdbcTemplate.batchUpdate(new String[]
			{ "UPDATE " + table + " SET state='F' ,effective=1 WHERE id='" + id + "'",
					"UPDATE usc_model_itemmenu SET state='F' WHERE del=0 AND itemid='" + id + "'",
					"UPDATE usc_model_classview_node SET state='F'  WHERE del=0 AND itemid='" + id + "'" });
			b = 1 == is[0];

			return b ? initModel(table, id) : false;
		}

		int[] is = jdbcTemplate.batchUpdate(new String[]
		{ "UPDATE " + table + " SET state='F' ,effective=1 WHERE id='" + id + "'",
				"UPDATE usc_model_itemmenu SET state='F' WHERE del=0 AND itemid='" + id + "'" });
		b = 1 == is[0];

		return b ? initModel(table, id) : false;
	}

	private boolean initModel(String table, String id)
	{
		boolean b = false;
		if (table.equals("usc_model_item"))
		{
			ItemInfo itemInfo = jdbcTemplate.queryForObject("SELECT * FROM usc_model_item WHERE id='" + id + "'",
					new ItemRowMapper());
			USCModelMate.removeItemInfoDataCache(itemInfo.getItemNo());
			USCModelMate.removeItemInfoDataByTableCache(itemInfo.getTableName());
			MateFactory.removeItemInfoCache(itemInfo.getItemNo());
			b = redis.hset("MODEL_ITEMDATA", itemInfo.getItemNo(), itemInfo);
			b = redis.hset("MODEL_ITEMDATABYTABLE", itemInfo.getTableName(), itemInfo);
		}
		if (table.equals("usc_model_classview"))
		{
			ModelClassView modelClassView = jdbcTemplate
					.queryForObject("SELECT * FROM " + table + " WHERE id='" + id + "'", new ModelClassViewRowMapper());
			USCModelMate.removClassViewDataCache(modelClassView.getNo());
			MateFactory.removClassViewCache(modelClassView.getNo());
			redis.hset("MODEL_QUERYVIEWDATA", modelClassView.getNo(), modelClassView);
		}
		if (table.equals("usc_model_relationship"))
		{
			ModelRelationShip relationShipInfo = jdbcTemplate
					.queryForObject("SELECT * FROM " + table + " WHERE id='" + id + "'", new RelationShipRowMapper());
			USCModelMate.removeRelationShipDataCache(relationShipInfo.getNo());
			MateFactory.removeRelationShipCache(relationShipInfo.getNo());
			redis.hset("MODEL_RELATIONSHIPDATA", relationShipInfo.getNo(), relationShipInfo);
		}
		if (table.equals("usc_model_queryview"))
		{
			ModelQueryView modelQueryView = jdbcTemplate
					.queryForObject("SELECT * FROM " + table + " WHERE id='" + id + "'", new ModelQueryViewRowMapper());
			USCModelMate.removQueryViewDataDataCache(modelQueryView.getNo());
			MateFactory.removQueryViewCache(modelQueryView.getNo());
			redis.hset("MODEL_QUERYVIEWDATA", modelQueryView.getNo(), modelQueryView);
		}
		return b;

	}

	private boolean addFields(List<String> slqs, String tableName, List<Map<String, Object>> fieldsC)
	{
		if (ObjectHelperUtils.isEmpty(tableName) || ObjectHelperUtils.isEmpty(fieldsC))
		{
			return false;
		}
		String alterTable = "ALTER TABLE ";
		String add = " ADD ";
		for (Map<String, Object> addField : fieldsC)
		{
			String field = getFieldSql(addField);
			field = field.substring(0, field.length() - 1);
			String alterSql = (alterTable + tableName + add + field);
			System.out.println(alterSql);
			slqs.add(alterSql);
		}
		return true;
	}

	private boolean modifiyFields(List<String> sqls, String tableName, List<Map<String, Object>> fieldsM)
	{
		if (ObjectHelperUtils.isEmpty(tableName) || ObjectHelperUtils.isEmpty(fieldsM))
		{
			return false;
		}
		String alterTable = "ALTER TABLE ";
		String modify = " MODIFY ";
		for (Map<String, Object> modifyField : fieldsM)
		{
			String field = getFieldSql(modifyField);
			field = field.substring(0, field.length() - 1);
			String alterSql = alterTable + tableName + modify + field;
			System.out.println(alterSql);
			sqls.add(alterSql);
		}
		return true;
	}

	protected void execute(String setString, String itemID, String condition)
	{
		condition = (condition != null) ? (" AND state='" + condition + "'") : "";
		String item = "UPDATE usc_model_item SET effective=1," + setString + " WHERE id ='" + itemID + "'" + condition;
		String field = "UPDATE usc_model_field SET " + setString + " WHERE del=0  AND itemid ='" + itemID + "'"
				+ condition;
		String itemmenu = "UPDATE usc_model_itemmenu SET " + setString + " WHERE del=0  AND itemid = '" + itemID + "'"
				+ condition;
		String grid = "UPDATE usc_model_grid SET " + setString + " WHERE del=0 AND itemid = '" + itemID + "'"
				+ condition;
		String gridField = "UPDATE usc_model_grid_field SET " + setString + " WHERE del=0 AND itemid ='" + itemID + "'"
				+ condition;
		String property = "UPDATE usc_model_property SET " + setString + " WHERE del=0 AND itemid ='" + itemID + "'"
				+ condition;
		String propertyField = "UPDATE usc_model_property_field SET " + setString + " WHERE del=0 AND itemid ='"
				+ itemID + "'" + condition;
		String relationPage = "UPDATE usc_model_relationpage SET " + setString + " WHERE del=0 AND itemid ='" + itemID
				+ "'" + condition;
		String relationPageSign = "UPDATE usc_model_relationpage_sign SET " + setString + " WHERE del=0 AND itemid ='"
				+ itemID + "'" + condition;
		String[] sql = new String[9];
		sql[0] = item;
		sql[1] = field;
		sql[2] = itemmenu;
		sql[3] = grid;
		sql[4] = gridField;
		sql[5] = property;
		sql[6] = propertyField;
		sql[7] = relationPage;
		sql[8] = relationPageSign;
		jdbcTemplate.batchUpdate(sql);

	}

	private String getCreateTableSql(Map<String, Object> itemMap)
	{
		String itemid = (String) itemMap.get("id");
		List<Map<String, Object>> fieldC = jdbcTemplate.queryForList("SELECT * FROM usc_model_field WHERE "
				+ "del=0 AND state='C' AND itemid='" + itemid + "' ORDER BY SORT");
		if (fieldC == null || fieldC.isEmpty())
			return null;
		String tableName = (String) itemMap.get("tablename");
		int type = (Integer) itemMap.get("type");
		String createL = "CREATE TABLE " + tableName + "(";
		String name = (String) itemMap.get("name");
		StringBuffer cSql = new StringBuffer("");
		String pk = "	PRIMARY KEY (id)";
		String tableRemark = ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '" + name + "' ";

		cSql.append(createL);
		for (Map<String, Object> fieldMap : fieldC)
		{
			String field = getFieldSql(fieldMap);
			cSql.append("\n	" + field);
		}
		cSql.append("\n" + pk);
		if (type == 2)
		{
			cSql.append(",");
			String sql2 = "\n" + "  KEY INDEX_" + tableName.toUpperCase() + "_ITEMA (itema) USING BTREE,\n"
					+ "  KEY INDEX_" + tableName.toUpperCase() + "_ITEMAID (itemaid) USING BTREE,\n" + "  KEY INDEX_"
					+ tableName.toUpperCase() + "_ITEMAB (itemb) USING BTREE,\n" + "  KEY INDEX_"
					+ tableName.toUpperCase() + "_ITEMBID (itembid) USING BTREE\n";
			cSql.append(sql2);
		} else if (type == 3)
		{
			cSql.append(",");
			String sql3 = "\n" + "  KEY INDEX_" + tableName.toUpperCase() + "_ITEMA (itemid) USING BTREE,\n"
					+ "  KEY INDEX_" + tableName.toUpperCase() + "_ITEMAID (nodeid) USING BTREE\n";
			cSql.append(sql3);
		}
		cSql.append(tableRemark + ";");
		System.out.println(cSql);
		return cSql.toString();

	}

	private String getFieldSql(Map<String, Object> fieldMap)
	{
		String fname = (String) fieldMap.get("fieldname"); // 字段名
		String name = (String) fieldMap.get("name"); // 字段名
		String ftype = (String) fieldMap.get("ftype"); // 字段类型
		Object flength = fieldMap.get("flength"); // 字段长度
		Object accuracy = fieldMap.get("accuracy"); // 字段精度

		String sql = "";
		if (ftype.equals(DATETIME))
		{
			sql = fname + " DATETIME DEFAULT NULL COMMENT '" + name + "',";
		} else if (ftype.equals(VARCHAR))
		{
			sql = fname.equals("id") ? (fname + " VARCHAR (" + flength + ") NOT NULL COMMENT '" + name + "',")
					: (fname + " VARCHAR (" + flength + ") DEFAULT NULL COMMENT '" + name + "',");
		} else if (ftype.equals(LONGTEXT))
		{
			sql = fname.equals("id") ? (fname + " LONGTEXT (" + flength + ") NOT NULL COMMENT '" + name + "',")
					: (fname + " VARCHAR (" + flength + ") DEFAULT NULL COMMENT '" + name + "',");
		} else if (ftype.equals(INT))
		{
			Object _flength = flength == null ? 11 : flength;
			sql = fname + " INT(" + _flength + ") DEFAULT NULL COMMENT '" + name + "',";
		} else if (ftype.equals(FLOAT))
		{
			Object _flength = flength == null ? 12 : flength;
			Object _accuracy = accuracy == null ? 1 : accuracy;
			sql = fname + " FLOAT(" + _flength + "," + _accuracy + ") DEFAULT NULL COMMENT '" + name + "',";
		} else if (ftype.equals(DOUBLE))
		{
			Object _flength = flength == null ? 13 : flength;
			Object _accuracy = accuracy == null ? 4 : accuracy;
			sql = fname + " DOUBLE(" + _flength + "," + _accuracy + ") DEFAULT NULL COMMENT '" + name + "',";
		} else if (ftype.equals(NUMBER))
		{
			Object _flength = flength == null ? 13 : flength;
			Object _accuracy = accuracy == null ? 4 : accuracy;
			sql = fname + " NUMERIC(" + _flength + "," + _accuracy + ") DEFAULT NULL COMMENT '" + name + "',";
		} else if (ftype.equals(BOOLEAN))
		{
			sql = fname + " INT(1) DEFAULT NULL COMMENT '" + name + "',";
		}
		return sql;

	}

	private String[] getModelTables()
	{
		return new String[]
		{ "usc_model_item", "usc_model_relationship", "usc_model_queryview", "usc_model_classview" };
	}
}
