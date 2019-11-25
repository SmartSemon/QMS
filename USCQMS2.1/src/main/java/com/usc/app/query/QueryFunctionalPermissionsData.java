package com.usc.app.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.cache.redis.RedisUtil;
import com.usc.obj.api.USCObject;
import com.usc.server.jdbc.SDBUtils;

public class QueryFunctionalPermissionsData extends AbstractAction
{
	JdbcTemplate jdbcTemplate = null;

	@Override
	public Object executeAction() throws Exception
	{
		List<Object> dataList = new ArrayList<Object>();
		USCObject object = context.getSelectedObj();
		if ("0".equals(object.getFieldValue("RTYPE")))
		{
			return StandardResultTranslate.getQueryResult(true, "Action_Default", null);
		}
		RedisUtil redisUtil = new RedisUtil();
		List<Map> list = (List<Map>) redisUtil.get("SYSTEM_PERMISSIONS");
		List<Object> list1 = new ArrayList<Object>();
		List<Object> list2 = new ArrayList<Object>();
		List<Map<String, Object>> listA = SDBUtils.getAuthData(object);
		if (listA != null)
		{
			for (Map<String, Object> map : listA)
			{

				Object CID = map.get("CID");
				if (1 == (Integer) map.get("CHECKEDSTATE"))
				{
					if (!list1.contains(CID))
					{
						list1.add(CID);
					}
				} else
				{
					if (!list2.contains(CID))
					{
						list2.add(CID);
					}
				}

			}
		}

		dataList.add(list);
		dataList.add(list1);
		dataList.add(list2);
		return StandardResultTranslate.getQueryResult(true, "Action_Default", dataList);
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
