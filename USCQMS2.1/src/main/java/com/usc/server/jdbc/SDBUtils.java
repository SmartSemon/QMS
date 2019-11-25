package com.usc.server.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.usc.obj.api.USCObject;
import com.usc.obj.api.bean.UserInformation;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.server.DBConnecter;
import com.usc.server.jdbc.base.DatabaseUtil;
import com.usc.server.util.SystemTime;
import com.usc.server.util.uuid.USCUUID;
import com.usc.util.ObjectHelperUtils;

public class SDBUtils
{

	public static boolean dataExistence(String table, String condition, Object... objects)
	{

		List<Map<String, Object>> datas = DBUtil.queryForList(table, condition, objects);
		if (!ObjectHelperUtils.isEmpty(datas))
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static Map getUserInfomation(String userName)
	{
		List<Map> data = null;
		try
		{
			data = DBUtil.getObjValues("SUSER", "del=? AND name=?", new Object[]
			{ 0, userName });
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (ObjectHelperUtils.isEmpty(data))
		{
			return null;
		}
		return data.get(0);
	}

	public static String getUserPS(String userName)
	{
		Map<String, Object> data = getUserInfomation(userName);
		if (ObjectHelperUtils.isEmpty(data))
		{
			return "-1";
		}
		return (String) data.get("PASSWORD");
	}

	public static List<Map<String, Object>> getAuthData(USCObject object)
	{
		String strID = object.getID();
		return DBUtil.getSQLResultByCondition("AUTHORIZED", "del=0 AND (checkedstate=? OR checkedstate=?) AND sruid=?",
				new Object[]
				{ 1, 2, strID }, new int[]
				{ Types.INTEGER, Types.INTEGER, Types.VARCHAR });
	}

	public static boolean authorize(USCObject uscObject, List<Map> nAuthData, ApplicationContext context)
	{
		if (ObjectHelperUtils.isEmpty(nAuthData))
		{
			return false;
		}
		List<Object[]> list = new ArrayList<Object[]>(nAuthData.size());
		StringBuffer fields = new StringBuffer("INSERT INTO authorized (");
		StringBuffer values = new StringBuffer(" VALUES (");

		for (int i = 0; i < nAuthData.size(); i++)
		{
			Map map = nAuthData.get(i);
			int size = map.keySet().size();
			Object[] objects = new Object[map.size() + 5];
			if (i == 0)
			{
				int j = 0;
				for (Object object : map.keySet())
				{

					if (j > 0)
					{
						fields.append(",");
						values.append(",");
					}
					if ("id".equals(object))
					{
						fields.append("cid");
					} else
					{
						fields.append((String) object);
					}

					values.append("?");

					objects[j] = map.get(object);
					j++;
				}

				fields.append(",sruid,id,del,cuser,ctime)");
				values.append(",?,?,?,?,?)");
			} else
			{
				int j = 0;
				for (Object object : map.keySet())
				{
					objects[j] = map.get(object);
					j++;
				}
			}
			objects[size] = uscObject.getID();
			objects[size + 1] = USCUUID.UUID();
			objects[size + 2] = 0;
			objects[size + 3] = context.getUserName();
			objects[size + 4] = SystemTime.getTimestamp();
			list.add(objects);

		}
		String sql = fields.append(values).toString();
		return DBUtil.BathInsertOrUpdate(sql, list);

	}

	public static void cancelAuthorize(USCObject object, List<String> hIDS, ApplicationContext context)
	{

		StringBuffer sql = new StringBuffer("UPDATE authorized SET del=?,dtime=?,duser=? WHERE sruid=? AND cid IN(");

		Object[] objects = new Object[hIDS.size()];
		for (int i = 0; i < hIDS.size(); i++)
		{
			if (i > 0)
			{
				sql.append(",");
			}
			sql.append("'" + hIDS.get(i) + "'");
			objects[i] = hIDS.get(i);
		}
		sql.append(")");
		Connection connection = null;
		PreparedStatement ps = null;
		try
		{
			connection = DBConnecter.getConnection();
			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, 1);
			ps.setTimestamp(2, SystemTime.getTimestamp());
			ps.setString(3, context.getUserName());
			ps.setString(4, object.getID());
			ps.execute();
		} catch (Exception e1)
		{
			e1.printStackTrace();
		} finally
		{
			DatabaseUtil.cleanUp(connection);
			DatabaseUtil.cleanUp(ps);
		}
	}

	public static void updateAuthorize(USCObject uscObject, List<String> hIDS, List<String> hHIDS, List<Map> nAuthData,
			ApplicationContext context)
	{
		Map<String, Integer> map = getAuthedData(hIDS, hHIDS);
		if (ObjectHelperUtils.isEmpty(map))
		{
			authorize(uscObject, nAuthData, context);
			return;
		}

		if (ObjectHelperUtils.isEmpty(nAuthData))
		{
			List<String> ids = new ArrayList<String>(hIDS);
			ids.addAll(hHIDS);
			cancelAuthorize(uscObject, ids, context);
			return;
		}

		List<Map> newAuthData = new ArrayList<Map>();
		List<Object[]> updateAuthData = new ArrayList<Object[]>();

		Timestamp uTime = SystemTime.getTimestamp();
		for (int i = 0; i < nAuthData.size(); i++)
		{
			String id = (String) nAuthData.get(i).get("id");
			if (!map.containsKey(id))
			{
				newAuthData.add(nAuthData.get(i));
//				nAuthData.remove(i);
			} else
			{
				int check = (Integer) nAuthData.get(i).get("checkedState");
				int checked = map.get(id);
				if (check != checked)
				{
					updateAuthData.add(new Object[]
					{ uTime, context.getUserName(), check, uscObject.getID(), id });
				}
				map.remove(id);
			}
		}

		if (!ObjectHelperUtils.isEmpty(newAuthData))
		{
			authorize(uscObject, newAuthData, context);
		}
		if (!ObjectHelperUtils.isEmpty(updateAuthData))
		{
			String sql = "UPDATE authorized SET mtime=?,muser=?,checkedstate=? WHERE sruid=? AND cid=?";

			DBUtil.BathInsertOrUpdate(sql, updateAuthData);
		}
		if (!ObjectHelperUtils.isEmpty(map))
		{
			List<String> dList = new ArrayList<String>();
			map.forEach((k, v) -> dList.add(k));
			cancelAuthorize(uscObject, dList, context);
		}
	}

	private static Map<String, Integer> getAuthedData(List<String> hIDS, List<String> hHIDS)
	{
		Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
		hIDS.forEach(k -> map.put(k, 1));
		hHIDS.forEach(k -> map.put(k, 2));
		return map;
	}

	public static List<String> getModuleAuthed(String userName)
	{
		Map map = getUserInfomation(userName);

		return moduleAuthedData((String) map.get("id"));

	}

	public static List<String> moduleAuthedData(String uID)
	{
		List<Map> list = getUserAuthAllData(uID);

		if (!ObjectHelperUtils.isEmpty(list))
		{
			List<String> list2 = new ArrayList<String>();
			list.forEach(map -> {
				String cid = (String) map.get("CID");
				cid = cid.substring(cid.lastIndexOf("_") + 1, cid.length());
				if (!list2.contains(cid))
				{
					list2.add(cid);
				}
			});
			return list2;

		}

		return null;
	}

	public static List<Map> getUserAuthAllData(String uID)
	{
		String itemA = "SROLE";
		String itemB = "SUSER";
		String condition = "del = 0 AND sruid IN (SELECT id FROM srole WHERE del=0 "
				+ "AND EXISTS(SELECT 1 FROM sr_srole_suser_obj WHERE DEL=0 "
				+ "AND itema=? AND itemb=? AND itembid=? AND itemaid=srole.id) UNION "
				+ "SELECT id FROM suser WHERE id=?)";

		Object[] objects = new Object[]
		{ itemA, itemB, uID, uID };
		int[] types = new int[]
		{ Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		List<Map> list = DBUtil.getSQLResultByCondition("AUTHORIZED", condition, objects, types);
		return list;
	}

	public static Map getUserWKContextID(UserInformation userInformation)
	{
		String userID = userInformation.getUserID();
		List maps = DBUtil.getSQLResultByCondition("wkclient", "del=0 AND suid='" + userID + "'");
		return ObjectHelperUtils.isEmpty(maps) ? null : (Map) maps.get(0);
	}

}
