package com.usc.server.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.validation.constraints.NotNull;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usc.app.execption.ThrowRemoteException;
import com.usc.server.DBConnecter;
import com.usc.server.jdbc.base.DataBaseUtils;
import com.usc.server.jdbc.base.DatabaseUtil;
import com.usc.server.md.ItemField;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.USCModelMate;
import com.usc.server.md.field.FieldAdapter;
import com.usc.server.md.field.FieldEditor;
import com.usc.server.md.field.FieldMappingConverter;
import com.usc.server.md.field.FieldUtils;
import com.usc.server.util.LoggerFactory;
import com.usc.server.util.StringHelperUtil;
import com.usc.server.util.SystemTime;
import com.usc.server.util.uuid.USCUUID;
import com.usc.util.ObjectHelperUtils;

/**
 * ClassName: DBUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: DataBase UtilClass(可选). <br/>
 * date: 2019年7月31日 下午4:33:35 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class DBUtil
{

	private static JdbcTemplate getJdbcTemplate()
	{
		try
		{
			return new JdbcTemplate(DBConnecter.getDataSource());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static boolean dropTable(String tableName)
	{
		ItemInfo info = USCModelMate.getItemInfoByTable(tableName);
		if (info != null)
		{
			if (info.getSitem() == 1)
			{
				ThrowRemoteException.throwException(new Throwable(tableName + " is System table , do not drop!"));
			}
		}
		String sql = "DROP TABLE " + tableName;
		try
		{
			getJdbcTemplate().execute(sql);
			return true;
		} catch (Exception e)
		{
			return false;
		}
	}

	public static Map<String, Object> insertRecord(Object itemObject, Object values, String user) throws Exception
	{
		if (itemObject == null || values == null)
			return null;
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}
//		Map valueMap = getValueMapByValues(values, 1, user);
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.putAll((Map<String, Object>) values);
		String tableName = itemInfo.getTableName();
		List<ItemField> fieldList = itemInfo.getItemFieldList();
		int nCount = 0;
		if (fieldList == null || fieldList.size() < 1)
		{
			throw new Exception("Fields infomation of table(" + tableName + ") has not been registed");
		}
		StringBuffer strSQL = new StringBuffer("INSERT INTO " + tableName + " (");
		for (ItemField field : fieldList)
		{
			String fieldNo = field.getNo();
			if (valueMap.containsKey(fieldNo))
			{
				nCount++;
				if (nCount > 1)
				{
					strSQL.append(",");
				}
				strSQL.append(field.getFieldName());
			}
		}
		strSQL.append(") VALUES(");
		for (int i = 0; i < nCount; i++)
		{
			if (i > 0)
			{
				strSQL.append(",");
			}
			strSQL.append("?");
		}
		strSQL.append(")");
		int row = 0;
		PreparedStatement ps = null;
		Connection connection = null;
		try
		{
			connection = DBConnecter.getConnection();
			if (strSQL == null || strSQL.toString() == null)
			{
				return null;
			}
			if (connection == null)
			{
				return null;
			}
			ps = connection.prepareStatement(strSQL.toString());
			int idx = 1;
			for (ItemField field : fieldList)
			{
				String fieldNo = field.getNo();
				Object value = null;
				if (valueMap.containsKey(fieldNo))
				{
					value = valueMap.get(fieldNo);
					if ((value != null) && ((value instanceof Date)) && (!(value instanceof Timestamp)))
					{
						value = new Timestamp(((Date) value).getTime());
					}
					setObjValueByFieldType(ps, value, idx, field.getFType());
					idx++;
				}

			}
			row = ps.executeUpdate();
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			DatabaseUtil.cleanUp(ps);
			DatabaseUtil.cleanUp(connection);
		}

		if (row != 1)
		{
			throw new Exception("create failed");
		}
		return valueMap;
	}

	public static List<Map> bathInsertRecords(Object itemObject, List<Map> list, String user) throws Exception
	{
		if (itemObject == null || list == null)
			return null;
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}

		String tableName = itemInfo.getTableName();
		List<ItemField> fieldList = itemInfo.getItemFieldList();
		int nCount = 0;
		if (fieldList == null || fieldList.size() < 1)
		{
			throw new Exception("Fields infomation of table(" + tableName + ") has not been registed");
		}

		StringBuffer strSQL = new StringBuffer("INSERT INTO " + tableName + " (");
		list = getNewMapList(list, 1, user);
		for (ItemField field : fieldList)
		{
			String fieldNo = field.getNo();
			if (list.get(0).containsKey(fieldNo))
			{
				nCount++;
				if (nCount > 1)
				{
					strSQL.append(",");
				}
				strSQL.append(field.getFieldName());

			}
		}
		strSQL.append(") VALUES(");
		for (int i = 0; i < nCount; i++)
		{
			if (i > 0)
			{
				strSQL.append(",");
			}
			strSQL.append("?");
		}
		strSQL.append(")");

		BatchPreparedStatementSetter bpSetter = new MyBPSetter(itemInfo, list);
		JdbcTemplate jdbcTemplate1 = new JdbcTemplate(DBConnecter.getDataSource());
		int[] row = jdbcTemplate1.batchUpdate(strSQL.toString(), bpSetter);
		if (row != null)
		{

		}

		return row != null ? list : null;
	}

	private static List<Map> getNewMapList(List<Map> list, int sign, String user) throws Exception
	{
		List<Map> maps = new ArrayList<Map>();
		for (Map map : list)
		{

			Map newMap = getValueMapByValues(map, sign, user);
			maps.add(newMap);
		}
		return maps;
	}

	static class MyBPSetter implements BatchPreparedStatementSetter
	{
		List<Map> maps = null;
		ItemInfo itemInfo = null;

		public MyBPSetter(ItemInfo itemInfo, List<Map> maps)
		{
			this.maps = maps;
			this.itemInfo = itemInfo;
		}

		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException
		{
			int idx = 1;
			for (ItemField field : itemInfo.getItemFieldList())
			{
				String fieldNo = field.getNo();
				Object value = null;
				Map newMap = maps.get(i);
				if (newMap.containsKey(fieldNo))
				{
					value = newMap.get(fieldNo);
					if ((value != null) && ((value instanceof Date)) && (!(value instanceof Timestamp)))
					{
						value = new Timestamp(((Date) value).getTime());
					}
					setObjValueByFieldType(ps, value, idx, field.getFType());
					idx++;
				}
			}

		}

		@Override
		public int getBatchSize()
		{
			return (this.maps == null || this.maps.isEmpty()) ? 0 : maps.size();
		}
	}

	public static boolean deleteRecord(Object itemObject, Object[] values) throws Exception
	{
		if (itemObject == null || values == null)
			return false;
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}

		try
		{
			Object[] objs = values;
			int[] types = new int[3];
			types[0] = Types.VARCHAR;
			types[1] = Types.TIMESTAMP;
			types[2] = Types.VARCHAR;
			String strSql = "UPDATE " + itemInfo.getTableName()
					+ " SET del=1,state='HS',mysm='D',duser=?,dtime=? WHERE id=?";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(DBConnecter.getDataSource());
			int row = jdbcTemplate.update(strSql, objs, types);
			if (row != 1)
			{
				return false;
			}
		} catch (Exception e)
		{
			throw new Exception("update failed");
		} finally
		{

		}

		return true;

	}

	public static boolean deleteRecord(JdbcTemplate jdbcTemplate, Object itemObject, String user, String id)
			throws Exception
	{
		if (itemObject == null || id == null)
			return false;
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}
		try
		{
			Object[] objs = new Object[3];
			objs[0] = user;
			objs[1] = new Timestamp(System.currentTimeMillis());
			objs[2] = id;
			int[] types = new int[3];
			types[0] = Types.VARCHAR;
			types[1] = Types.TIMESTAMP;
			types[3] = Types.VARCHAR;
			String strSql = "UPDATE " + itemInfo.getTableName()
					+ " SET del=1,state='HS',mysm='D',duser=?,dtime=? WHERE id=?";

			JdbcTemplate jdbcTemplate1 = new JdbcTemplate(DBConnecter.getDataSource());
			int row = jdbcTemplate1.update(strSql, objs, types);
			if (row != 1)
			{
				return false;
			}
		} catch (Exception e)
		{
			throw new Exception("update failed");
		}

		return true;

	}

	public static boolean PhysicalDeleteRecord(JdbcTemplate jdbcTemplate, String tableName, String id)
	{
		Connection connection = null;
		PreparedStatement ps = null;
		try
		{
			String strSql = "DELETE FROM " + tableName + " WHERE id=?";

			connection = DBConnecter.getConnection();
			ps = connection.prepareStatement(strSql);
			int row = ps.executeUpdate();
//			int row = jdbcTemplate.update(strSql, id, Types.VARCHAR);
			if (row != 1)
			{
				return false;
			}
		} catch (Exception e)
		{
			LoggerFactory.logError("delete failed", e);
		} finally
		{
			DatabaseUtil.cleanUp(ps);
			DatabaseUtil.cleanUp(connection);
		}

		return true;

	}

	public static boolean saveRecord(JdbcTemplate jdbcTemplate, Object itemObject, String strID, Object values,
			String user) throws Exception
	{
		if (itemObject == null || values == null)
			return false;
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}
		Map valueMap = getValueMapByValues(values, 2, user);
		String tableName = itemInfo.getTableName();
		List<ItemField> fieldList = itemInfo.getItemFieldList();
		int nCount = 0;
		if (fieldList == null || fieldList.size() < 1)
		{
			throw new Exception("Fields infomation of table(" + tableName + ") has not been registed");
		}
		StringBuffer strSQL = new StringBuffer("UPDATE " + tableName + " SET ");
		for (ItemField field : fieldList)
		{
			String fieldNo = field.getNo();
			if (!fieldNo.equals("id"))
			{
				if (valueMap.containsKey(fieldNo))
				{
					nCount++;
					if (nCount > 1)
					{
						strSQL.append(",");
					}
					strSQL.append(field.getFieldName() + "=?");
				}
			}

		}
		strSQL.append(" WHERE id='" + strID + "'");
		PreparedStatement ps = null;
		Connection connection = null;
		try
		{
			connection = DBConnecter.getConnection();
			ps = connection.prepareStatement(strSQL.toString());
			int idx = 1;
			for (ItemField field : fieldList)
			{
				String fieldNO = field.getNo();
				Object value = null;
				if (!fieldNO.equals("id"))
				{
					if (valueMap.containsKey(fieldNO))
					{
						value = valueMap.get(fieldNO);
						if ((value != null) && ((value instanceof Date)) && (!(value instanceof Timestamp)))
						{
							value = new Timestamp(((Date) value).getTime());
						} else if ((value != null) && ((value instanceof String)) || (value instanceof Integer))
						{
							String editor = field.getEditor();
							if (FieldEditor.MAPVALUELIST.equals(editor))
							{
								value = FieldMappingConverter.getValue2Key(field, value);
							}
						}
						setObjValueByFieldType(ps, value, idx, field.getFType());
						idx++;
					}
				}

			}
			int i = ps.executeUpdate();
			return i == 1 ? true : false;
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			DatabaseUtil.cleanUp(connection);
			DatabaseUtil.cleanUp(ps);
		}

	}

	public static boolean saveDBRecord(Object itemObject, String strID, Map<String, Object> values) throws Exception
	{
		if (itemObject == null || values == null)
			return false;
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}
		Map valueMap = values;
		String tableName = itemInfo.getTableName();
		List<ItemField> fieldList = itemInfo.getItemFieldList();
		int nCount = 0;
		if (fieldList == null || fieldList.size() < 1)
		{
			LoggerFactory.logError("err",
					new Throwable("Fields infomation of table(" + tableName + ") has not been registed"));
		}
		StringBuffer strSQL = new StringBuffer("UPDATE " + tableName + " SET ");
		for (ItemField field : fieldList)
		{
			String fieldNo = field.getNo();
			if (!fieldNo.equals("id"))
			{
				if (valueMap.containsKey(fieldNo))
				{
					nCount++;
					if (nCount > 1)
					{
						strSQL.append(",");
					}
					strSQL.append(field.getFieldName() + "=?");
				}
			}

		}
		strSQL.append(" WHERE id='" + strID + "'");
		PreparedStatement ps = null;
		Connection connection = null;
		try
		{
			connection = DBConnecter.getConnection();
			ps = connection.prepareStatement(strSQL.toString());
			int idx = 1;
			for (ItemField field : fieldList)
			{
				String fieldNO = field.getNo();
				Object value = null;
				if (!fieldNO.equals("id"))
				{
					if (valueMap.containsKey(fieldNO))
					{
						value = valueMap.get(fieldNO);
						if ((value != null) && ((value instanceof Date)) && (!(value instanceof Timestamp)))
						{
							value = new Timestamp(((Date) value).getTime());
						} else if ((value != null) && ((value instanceof String)) || (value instanceof Integer))
						{
							String editor = field.getEditor();
							if (FieldEditor.MAPVALUELIST.equals(editor))
							{
								value = FieldMappingConverter.getValue2Key(field, value);
							}
						}
						setObjValueByFieldType(ps, value, idx, field.getFType());
						idx++;
					}
				}

			}
			int i = ps.executeUpdate();
			return i == 1 ? true : false;
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			DatabaseUtil.cleanUp(connection);
			DatabaseUtil.cleanUp(ps);
		}

	}

	public static boolean BathSaveRecord(JdbcTemplate jdbcTemplate, Object itemObject, String strID,
			List<Object[]> values, String user) throws Exception
	{
		if (itemObject == null || values == null)
			return false;
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}
		Map valueMap = getValueMapByValues(values, 2, user);
		String tableName = itemInfo.getTableName();
		List<ItemField> fieldList = itemInfo.getItemFieldList();
		int nCount = 0;
		if (fieldList == null || fieldList.size() < 1)
		{
			throw new Exception("Fields infomation of table(" + tableName + ") has not been registed");
		}
		StringBuffer strSQL = new StringBuffer("UPDATE " + tableName + " SET ");
		for (ItemField field : fieldList)
		{
			String tableField = field.getFieldName();
			if (!tableField.equals("id"))
			{
				if (valueMap.containsKey(tableField))
				{
					nCount++;
					if (nCount > 1)
					{
						strSQL.append(",");
					}
					strSQL.append(tableField + "=?");
				}
			}

		}
		strSQL.append(" WHERE id='" + strID + "'");
		PreparedStatement ps = null;
		Connection connection = null;
		try
		{
			connection = jdbcTemplate.getDataSource().getConnection();
			ps = connection.prepareStatement(strSQL.toString());
			int idx = 1;
			for (ItemField field : fieldList)
			{
				String tableField = field.getFieldName();
				Object value = null;
				if (!tableField.equals("id"))
				{
					if (valueMap.containsKey(tableField))
					{
						value = valueMap.get(tableField);
						if ((value != null) && ((value instanceof Date)) && (!(value instanceof Timestamp)))
						{
							value = new Timestamp(((Date) value).getTime());
						} else if ((value != null) && ((value instanceof String)) || (value instanceof Integer))
						{
							String editor = field.getEditor();
							if (FieldEditor.MAPVALUELIST.equals(editor))
							{
								value = FieldMappingConverter.getValue2Key(field, value);
							}
						}
						setObjValueByFieldType(ps, value, idx, field.getFType());
						idx++;
					}
				}

			}
			return ps.executeUpdate() == 1 ? true : false;
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			DatabaseUtil.cleanUp(connection);
			DatabaseUtil.cleanUp(ps);
		}

	}

	public static Map getObjValuesByID(Object itemObject, String strID) throws Exception
	{
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}
		String tableName = itemInfo.getTableName();
		StringBuffer strSQL = new StringBuffer("SELECT ");
		List<ItemField> fields = itemInfo.getItemFieldList();
		for (int i = 0; i < fields.size(); i++)
		{
			ItemField field = fields.get(i);
			String fieldName = field.getFieldName();
			if (i > 0)
				strSQL.append(",");
			strSQL.append(fieldName);
		}
		strSQL.append(" FROM " + tableName + " WHERE id=?");
		PreparedStatement ps = null;
		Connection connection = null;
		try
		{
			connection = DBConnecter.getDataSource().getConnection();
			ps = connection.prepareStatement(strSQL.toString());
			ps.setObject(1, strID);
			ResultSet rs = ps.executeQuery();
			if (rs != null)
			{
				return (Map) getResultSet(itemInfo, rs).get(0);
			}
		} finally
		{
			DatabaseUtil.cleanUp(connection);
			DatabaseUtil.cleanUp(ps);
		}
		return null;

	}

	public static List<Map> getObjValues(Object itemObject, String condition, Object... objects) throws Exception
	{
		ItemInfo itemInfo = null;
		if (itemObject instanceof ItemInfo)
		{
			itemInfo = (ItemInfo) itemObject;
		} else if (itemObject instanceof String)
		{
			itemInfo = USCModelMate.getItemInfo(String.valueOf(itemObject));
		}
		if (itemInfo == null)
		{
			throw new Exception("ItemInfo of " + itemObject + " UNFIND");
		}
		String tableName = itemInfo.getTableName();
		StringBuffer strSQL = new StringBuffer("SELECT ");
		List<ItemField> fields = itemInfo.getItemFieldList();
		for (int i = 0; i < fields.size(); i++)
		{
			ItemField field = fields.get(i);
			String fieldName = field.getFieldName();
			if (i > 0)
				strSQL.append(",");
			strSQL.append(fieldName);
		}
		strSQL.append(" FROM " + tableName + " WHERE del=0 AND (" + condition + ")");
		Connection connection = DBConnecter.getConnection();

		return getRowSet(connection, strSQL.toString(), fields, objects);

	}

	private static List<Map> getRowSet(Connection connection, String sql, List<ItemField> fields, Object... objects)

	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = connection.prepareStatement(sql);
			for (int j = 0; j < objects.length; j++)
			{
				ps.setObject(j + 1, objects[j]);
			}
			rs = ps.executeQuery();
			return convertList(rs, fields);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			DatabaseUtil.cleanUp(connection);
			DatabaseUtil.cleanUp(ps);
		}
		return null;

	}

	private static List convertList(ResultSet rs, List<ItemField> fields) throws Exception
	{
		List list = new Vector();
		ResultSetMetaData md = rs.getMetaData();
		while (rs.next())
		{
			Map rowData = new HashMap();
			for (int i = 1; i <= fields.size(); i++)
			{
				Object value = FieldUtils.getObjectByType(rs, i, fields.get(i - 1));
				rowData.put(fields.get(i - 1).getNo(), value);
			}
			list.add(rowData);
		}
		return list.isEmpty() ? null : list;
	}

	private static Map getValueMapByValues(Object values, int sign, String user) throws Exception
	{
		Map<String, Object> valueMap = new HashMap<String, Object>();
		if (values instanceof Map)
		{
			valueMap.putAll((Map) values);
		} else if (values instanceof JSONObject)
		{
			JSONObject jsonObject = (JSONObject) values;
			valueMap.putAll(JSONObject.parseObject(jsonObject.toJSONString(), Map.class));
		} else if (values instanceof String)
		{
			try
			{
				valueMap.putAll(JSONObject.parseObject((String) values, Map.class));
			} catch (Exception e)
			{
				throw new Exception("values of " + values + " Not in com.alibaba.fastjson.JSON format");
			}

		} else
		{
			throw new Exception("values of " + values + " Non-conformity");
		}
		if (sign == 1)
		{
			valueMap.putIfAbsent("id", USCUUID.UUID());
			valueMap.putIfAbsent("del", 0);
			valueMap.putIfAbsent("mysm", "N");
			valueMap.putIfAbsent("cuser", user);
			valueMap.putIfAbsent("ctime", SystemTime.getTimestamp());
			valueMap.putIfAbsent("state", "C");
		} else if (sign == 2)
		{
			valueMap.putIfAbsent("mysm", "M");
			valueMap.putIfAbsent("muser", user);
			valueMap.putIfAbsent("mtime", SystemTime.getTimestamp());

		} else
		{
			valueMap.putIfAbsent("del", 1);
			valueMap.putIfAbsent("mysm", "D");
			valueMap.putIfAbsent("duser", user);
			valueMap.putIfAbsent("dtime", SystemTime.getTimestamp());
			valueMap.putIfAbsent("state", "HS");
		}

		return valueMap;
	}

	private static void setObjValueByFieldType(PreparedStatement ps, Object value, int idx, String fType)
			throws SQLException
	{

		String ft = fType.trim().toUpperCase();
		if (value == null)
		{
			if (FieldAdapter.isInt(ft))
			{
				ps.setNull(idx, Types.INTEGER);
			} else if (FieldAdapter.isVarchar(ft))
			{
				ps.setString(idx, null);
			} else if (FieldAdapter.isFloat(ft))
			{
				ps.setNull(idx, Types.FLOAT);
			} else if (FieldAdapter.isDouble(ft))
			{
				ps.setNull(idx, Types.DOUBLE);
			} else if (FieldAdapter.isNumeric(ft))
			{
				ps.setNull(idx, Types.NUMERIC);
			} else if (FieldAdapter.isBoolean(ft))
			{
				ps.setNull(idx, Types.BOOLEAN);
			} else if (FieldAdapter.isDateTime(ft))
			{
				ps.setTimestamp(idx, null);
			} else
			{
				ps.setObject(idx, null);
			}
		} else
		{
			if (value instanceof Boolean)
			{
				boolean b = ((Boolean) value).booleanValue();
				value = new Integer(0);
				if (b)
				{
					value = new Integer(1);
				}
			}
			ps.setObject(idx, value);
		}

	}

	public static Object getMapValue(ItemField field, Object value)
	{
		if (value == null)
			return null;
		String param = field.getEditParams();
		param = param == null ? null : param.trim();
		if (param == null)
			return value;

		try
		{
			Map vMap = JSONArray.parseObject(param, Map.class);
			if (vMap.containsKey(value))
			{
				return value;
			}
			for (Object object2 : vMap.keySet())
			{
				if (value.equals(vMap.get(object2).toString()))
				{
					return object2;
				}
			}
		} catch (Exception e)
		{

		}
		return value;
	}

	public static String getItemQueryFieldsSql(ItemInfo info, String queryWord) throws Exception
	{
		String queryFields = info.getQueryFields();
		if (queryFields == null)
		{
			return null;
		}
		String[] fields = queryFields.split(",");
		StringBuffer buffer = new StringBuffer(" (");
		int nCount = 0;
		for (String string : fields)
		{
			ItemField field = info.getItemField(string);
			if (field != null)
			{
				nCount++;
				if (nCount > 1)
				{
					buffer.append(" OR ");
				}
				String tableField = field.getFieldName();
				String objString = setObjValueByFieldType(field, queryWord);
				buffer.append(tableField + " " + objString);
			}
		}
		buffer.append(")");
		return buffer.toString();
	}

	public static String setObjValueByFieldType(ItemField field, String value)
	{
		String vString = null;
		String editor = field.getEditor();
		if ("MapValueList".equals(editor))
		{
//			getMapValue(field, value);
			value = field.getFieldEditor().getMapValueList().get(value).toString();
		}
		String ft = field.getFType().trim().toUpperCase();
		if (value != null)
		{
			if (FieldAdapter.isInt(ft))
			{
				return "LIKE '%" + value + "%'";
			} else if (FieldAdapter.isVarchar(ft))
			{
				return "LIKE '%" + value + "%'";
			} else if (FieldAdapter.isFloat(ft))
			{
				return "LIKE '%" + value + "%'";
			} else if (FieldAdapter.isDouble(ft))
			{
				return "LIKE '%" + value + "%'";
			} else if (FieldAdapter.isNumeric(ft))
			{
				return "LIKE '%" + value + "%'";
			} else if (FieldAdapter.isBoolean(ft))
			{
				if (StringHelperUtil.isDigit(value))
				{
					if (Integer.valueOf(value) == 0 || Integer.valueOf(value) == 1)
					{
						return "=" + value;
					} else
					{
						return "LIKE '%" + value + "%'";
					}
				}
			} else if (FieldAdapter.isDateTime(ft))
			{
			} else
			{
			}
		}
		return vString;

	}

	public static List getSQLResult(String itemNo, String sql, List paramList)
	{
		if (itemNo == null || sql == null)
		{
			return null;
		}
		PreparedStatement localPreparedStatement = null;
		Connection connection = null;
		try
		{
			connection = DBConnecter.getConnection();
			localPreparedStatement = DBConnecter.getDataSource().getConnection().prepareStatement(sql);
			ItemInfo info = USCModelMate.getItemInfo(itemNo);
			if (ObjectHelperUtils.isEmpty(paramList))
			{
				paramList = info.getItemFieldList();
			}
			if (!sql.toLowerCase().contains("order"))
			{
				sql += " ORDER BY id DESC";
			}
			ResultSet localResultSet = DatabaseUtil.executeQuery(localPreparedStatement, sql);
			return getResultSet(info, localResultSet);
		} catch (Exception localException)
		{
			localException.printStackTrace();
		} finally
		{
			DatabaseUtil.cleanUp(localPreparedStatement);
			DatabaseUtil.cleanUp(connection);
		}
		return null;
	}

	public static List getSQLResultByCondition(Object param, String paramCondition)
	{

		PreparedStatement localPreparedStatement = null;
		Connection connection = null;
		try
		{

			ItemInfo itemInfo = null;
			if (param instanceof ItemInfo)
			{
				itemInfo = (ItemInfo) param;
			} else if (param instanceof String)
			{
				itemInfo = USCModelMate.getItemInfo((String) param);
			} else
			{
				LoggerFactory.logError("error : Type conversion exception or param Incorrect type ",
						new Throwable("error : Type conversion exception or param Incorrect type "));
			}

			Assert.notNull(itemInfo, "itemInfo must not be null");

			connection = DBConnecter.getConnection();
			localPreparedStatement = connection.prepareStatement(getSelectSql(itemInfo, paramCondition));
			ResultSet localResultSet = localPreparedStatement.executeQuery();
			return getResultSet(itemInfo, localResultSet);
		} catch (Exception localException)
		{
			localException.printStackTrace();
		} finally
		{
			DatabaseUtil.cleanUp(localPreparedStatement);
			DatabaseUtil.cleanUp(connection);
		}
		return null;
	}

	public static List getSQLResultByCondition(Object param, String paramCondition, Object[] objects, int[] types)
	{

		PreparedStatement localPreparedStatement = null;
		Connection connection = null;
		try
		{

			ItemInfo itemInfo = null;
			if (param instanceof ItemInfo)
			{
				itemInfo = (ItemInfo) param;
			} else if (param instanceof String)
			{
				itemInfo = USCModelMate.getItemInfo((String) param);
			} else
			{
				LoggerFactory.logError("error",
						new Throwable("Caused by: Type conversion exception or param Incorrect type "));
			}

			connection = DBConnecter.getConnection();
			localPreparedStatement = connection.prepareStatement(getSelectSql(itemInfo, paramCondition));
			if (objects != null && objects.length > 0)
			{
				if (types == null)
				{
					for (int j = 0; j < objects.length; j++)
					{
						localPreparedStatement.setObject(j + 1, "'%" + objects[j] + "%'");
					}
				} else
				{
					if (types.length != objects.length)
					{
						LoggerFactory.logError(SQLException.class, "error", new Throwable(
								"Caused by: The length of parameter array and type array is inconsistent"));
						return null;
					}
					for (int j = 0; j < objects.length; j++)
					{
						localPreparedStatement.setObject(j + 1, objects[j], types[j]);
					}
				}
			}
			ResultSet localResultSet = localPreparedStatement.executeQuery();
			return getResultSet(itemInfo, localResultSet);
		} catch (Exception localException)
		{
			localException.printStackTrace();
			return null;
		} finally
		{
			DatabaseUtil.cleanUp(localPreparedStatement);
			DatabaseUtil.cleanUp(connection);
		}
	}

	public static List getSQLResultByConditionLimit(Object param, String paramCondition, Object[] objects, int[] types,
			int page)
	{

		String sql2 = paramCondition == null ? "" : paramCondition;
		if (!sql2.toLowerCase().contains("limit") && !sql2.toLowerCase().contains("order"))
		{
			sql2 += " ORDER BY id DESC " + DataBaseUtils.getLimit(page);
		} else if (sql2.toLowerCase().contains("limit") && !sql2.toLowerCase().contains("order"))
		{
			String s = paramCondition.substring(0, sql2.indexOf("limit"));
			sql2 = s + " ORDER BY id DESC " + DataBaseUtils.getLimit(page);
		}
		return getSQLResultByCondition(param, paramCondition, objects, types);
	}

	public static List getSQLResultByConditionLimit(Object param, String paramCondition, int page)
	{

		String sql2 = paramCondition == null ? "" : paramCondition;
		if (!sql2.toLowerCase().contains("limit") && !sql2.toLowerCase().contains("order"))
		{
			sql2 += " ORDER BY id DESC " + DataBaseUtils.getLimit(page);
		} else if (sql2.toLowerCase().contains("limit") && !sql2.toLowerCase().contains("order"))
		{
			String s = paramCondition.substring(0, sql2.indexOf("limit"));
			sql2 = s + " ORDER BY id DESC " + DataBaseUtils.getLimit(page);
		}
		return getSQLResultByCondition(param, sql2);
	}

	public static List getResultSet(ItemInfo info, ResultSet resultSet) throws SQLException, Exception
	{
		List<ItemField> fieldList = info.getItemFieldList();
		int i = fieldList.size();
		List<Map<String, Object>> resultList = new Vector<Map<String, Object>>();
		while (resultSet.next())
		{
			Map<String, Object> localHashMap = new HashMap<String, Object>();
			for (int j = 1; j < i + 1; j++)
			{
				ItemField field = fieldList.get(j - 1);
//				Object localObject1 = FieldUtils.getObjectByType(resultSet, j, field);
				Object localObject1 = resultSet.getObject(j);
				String str2 = field.getNo();
				if ((localObject1 instanceof String))
				{
					localHashMap.put(str2, ((String) localObject1));
				} else
				{
					localHashMap.put(str2, localObject1);
				}
			}
			localHashMap.put("USC_OBJECT", info.getItemNo());
			resultList.add(localHashMap);
		}
		return resultList;

	}

	public static List getSQLResultByOnlyFieldObject(ItemInfo itemInfo, Map map) throws Exception
	{
		List<ItemField> fields = itemInfo.getItemFieldList();
		StringBuffer sfields = new StringBuffer("SELECT ");
		StringBuffer condition = new StringBuffer(" del=0 ");
		List<ItemField> onlyFields = USCModelMate.getOnlyItemFields(itemInfo);
		if (onlyFields != null)
		{
			Object[] objects = new Object[onlyFields.size()];
			int[] types = new int[onlyFields.size()];
			int i = 0;
			for (ItemField itemField : onlyFields)
			{
				objects[i] = getFieldValueByFieldType(itemField, map);
				if (i != 0)
				{
					sfields.append(",");
				}
				sfields.append(itemField.getFieldName()).append(" AS ").append(itemField.getNo());
				i++;
				condition.append("AND " + itemField.getFieldName() + "=? ");
			}
			String sql = sfields.append(" FROM ").append(itemInfo.getTableName()).append(" WHERE ").append(condition)
					.toString();
			JdbcTemplate jdbcTemplate1 = new JdbcTemplate(DBConnecter.getDataSource());
			List<Map<String, Object>> list = jdbcTemplate1.queryForList(sql, objects);
			return list;
		}

		return null;
	}

	private static Object getFieldValueByFieldType(ItemField itemField, Object paramObject) throws Exception
	{
		if (paramObject == null)
			return null;
		Object object = null;
		if (paramObject instanceof Map)
		{
			object = ((Map) paramObject).get(itemField.getNo());
		} else
		{
			object = paramObject;
		}

		return FieldUtils.getObjectByType(object, itemField);
	}

	public static List getRelationItemResult(ItemInfo itemA, ItemInfo itemB, ItemInfo relationItemInfo, String itemAID,
			int page)
	{
		if (itemA == null || itemB == null || relationItemInfo == null)
		{
			return null;
		}
		List<ItemField> fields = itemB.getItemFieldList();

		return getRelationItemResult(itemA.getTableName(), itemB, relationItemInfo.getTableName(), itemAID, page);
	}

	public static List getRelationItemResult(String tableA, ItemInfo itemB, String relTable, String itemAID, int page)
	{
		String querySql = "del=0 AND EXISTS(SELECT 1 FROM " + relTable + " WHERE " + "del=0 AND itema=? AND itemb=? "
				+ "AND itembid=" + itemB.getTableName() + ".id AND itemaid=?)";
		Object[] objects = new Object[]
		{ tableA, itemB.getTableName(), itemAID };
		int[] types =
		{ Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		return getSQLResultByConditionLimit(itemB, querySql, objects, types, page);
	}

	public static List<Map<String, Object>> queryForList(String table, String condition, Object... objects)
	{
		JdbcTemplate jdbcTemplate = null;
		try
		{
			jdbcTemplate = new JdbcTemplate(DBConnecter.getDataSource());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		String sql = "SELECT * FROM " + table + " WHERE " + condition;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, objects);
		return list;
	}

	public static Map<String, Object> queryForMap(String table, String condition, Object... objects)
	{
		JdbcTemplate jdbcTemplate = null;
		try
		{
			jdbcTemplate = new JdbcTemplate(DBConnecter.getDataSource());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		String sql = "SELECT * FROM " + table + " WHERE " + condition;
		Map<String, Object> data = jdbcTemplate.queryForMap(sql, objects);
		return data;
	}

	public static Map<String, Object> queryForMap(String sql, Object... objects)
	{
		try
		{
			Assert.notNull(sql);
		} catch (Exception e)
		{
			return null;
		}
		JdbcTemplate jdbcTemplate = null;
		try
		{
			jdbcTemplate = new JdbcTemplate(DBConnecter.getDataSource());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Map<String, Object> data = jdbcTemplate.queryForMap(sql, objects);
		return data;
	}

	public static List<Map<String, Object>> queryForList(@NotNull String sql, Object... objects)
	{
		JdbcTemplate jdbcTemplate = null;
		try
		{
			jdbcTemplate = new JdbcTemplate(DBConnecter.getDataSource());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql, objects);
		return dataList;
	}

	public static boolean insertOrUpdate(String insertSql, Object[] objects)
	{
		Connection connection = null;
		PreparedStatement ps = null;
		try
		{
			connection = DBConnecter.getConnection();
			ps = connection.prepareStatement(insertSql);
			for (int j = 0; j < objects.length; j++)
			{
				ps.setObject(j + 1, objects[j]);
			}
			return ps.executeUpdate() > 0;

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			DatabaseUtil.cleanUp(connection);
			DatabaseUtil.cleanUp(ps);
		}
		return false;
	}

	public static boolean BathInsertOrUpdate(String sql, List<Object[]> objects)
	{
		JdbcTemplate jdbcTemplate = null;
		try
		{
			jdbcTemplate = new JdbcTemplate(DBConnecter.getDataSource());
			int[] is = jdbcTemplate.batchUpdate(sql, objects);
			return is[0] > 0;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static String getSelectSql(ItemInfo itemInfo)
	{
		List<ItemField> fieldList = itemInfo.getItemFieldList();
		int i = fieldList.size();
		StringBuffer sqlBuffer = new StringBuffer("SELECT ");
		for (int m = 0; m < i; m++)
		{
			sqlBuffer.append(fieldList.get(m).getFieldName());
			if (m != (i - 1))
			{
				sqlBuffer.append(",");
			}
		}
		sqlBuffer.append(" FROM " + itemInfo.getTableName());
		return sqlBuffer.toString();
	}

	public static String getSelectSql(ItemInfo itemInfo, String paramCondition)
	{
		String sql = getSelectSql(itemInfo);
		if (paramCondition != null && !"".equals(paramCondition.replace("null", "")))
		{
			if (!paramCondition.trim().toLowerCase().startsWith("limit")
					&& !paramCondition.trim().toLowerCase().startsWith("order"))
			{
				sql += " WHERE ";
			}
			sql += paramCondition;
		}
		return sql;
	}
}
