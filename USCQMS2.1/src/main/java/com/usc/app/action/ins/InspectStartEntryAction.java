package com.usc.app.action.ins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.obj.api.USCObject;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.md.USCModelMate;
import com.usc.util.ObjectHelperUtils;

public class InspectStartEntryAction extends AbstractAction
{

	@SuppressWarnings("unchecked")
	@Override
	public Object executeAction() throws Exception
	{
		if (disable())
		{
			USCObject object = context.getSelectedObj();
			if ((Integer) object.getFieldValue("completed") == 1)
			{
				return StandardResultTranslate.getResult("当前申请单已存在检验结果", false);
			}
			String aID = object.getID();
			int num = (int) object.getFieldValue("num");
			List<Map> insproitem = DBUtil.getRelationItemResult(object.getTableName(),
					USCModelMate.getItemInfo("insproitem"), "rel_insproitem_obj", aID, 1);
			if (ObjectHelperUtils.isEmpty(insproitem))
			{
				return StandardResultTranslate.getResult("当前申请单未定义实际检验项目", false);
			}
			Map result = StandardResultTranslate.getResult("成功获取待检信息", true);
			result.put("inspectItems", insproitem);
			List<Map> waitobjs = DBUtil.getRelationItemResult(object.getTableName(),
					USCModelMate.getItemInfo("waitcheckobj"), "rel_waitcheckobj_obj", aID, 1);
			List<String> nos = new ArrayList<String>();
			if (ObjectHelperUtils.isEmpty(waitobjs))
			{
				for (int i = 0; i < num; i++)
				{
					int no = i + 1;
					nos.add(String.valueOf(no));
				}
			} else
			{
				waitobjs.forEach(map -> nos.add(String.valueOf(map.get("no"))));
			}
			result.put("serialNumbers", nos);
			return result;
		}
		return null;
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
