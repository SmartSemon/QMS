package com.usc.app._log.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.server.jdbc.DBUtil;
import com.usc.server.jdbc.SDBUtils;
import com.usc.util.ObjectHelperUtils;

public class Navigation
{

	public static List<Map<String, Object>> getAuthorized(String userName)
	{
		List<String> authIDS = SDBUtils.getModuleAuthed(userName);
		if (!ObjectHelperUtils.isEmpty(authIDS))
		{
			List<Map> maps = new ArrayList<Map>();
			StringBuffer sql = new StringBuffer("del=? AND state=? AND id IN(");
			Object[] objects = new Object[authIDS.size() + 2];
			objects[0] = 0;
			objects[1] = "F";
			for (int i = 0; i < authIDS.size(); i++)
			{
				if (i > 0)
				{
					sql.append(",");
				}
				sql.append("?");
				Object id = authIDS.get(i);
				objects[i + 2] = id;
			}
			sql.append(") ORDER BY sort");
			return DBUtil.queryForList("usc_model_navigation", sql.toString(), objects);

		}
		return null;
	}

}
