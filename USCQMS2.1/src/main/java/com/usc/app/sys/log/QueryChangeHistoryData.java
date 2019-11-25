package com.usc.app.sys.log;

import java.sql.Types;
import java.util.List;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.obj.api.USCObject;
import com.usc.server.jdbc.DBUtil;

public class QueryChangeHistoryData extends AbstractAction
{
	@Override
	public Object executeAction() throws Exception
	{
		USCObject object = context.getSelectedObj();
		String OBJTN = object.getTableName();
		String OBJID = object.getID();
		String condition = "action=? AND objtn=? AND objid=? ";
		List dataList = DBUtil.getSQLResultByCondition("SYSLOG", condition, new Object[]
		{ "MODIFY", OBJTN, OBJID }, new int[]
		{ Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });

		return StandardResultTranslate.getQueryResult(true, "Action_Query", dataList);
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
