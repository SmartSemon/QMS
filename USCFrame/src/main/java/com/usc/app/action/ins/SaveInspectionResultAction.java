package com.usc.app.action.ins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.usc.app.action.GetRelationData;
import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.NewUSCObjectHelper;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.util.SystemTime;
import com.usc.util.ObjectHelperUtils;

public class SaveInspectionResultAction extends AbstractAction
{
	private final String itemObj = "inspectionresult";
	private final String relationObj = "rel_inspectionresult_obj";

	@Override
	public Object executeAction() throws Exception
	{
		USCObject applyObj = context.getSelectedObj();
		String dataString = (String) context.getExtendInfo("otherParam");
		JSONArray jsonArray = JSONArray.parseArray(dataString);
		List<Map> newDatas = new ArrayList<Map>();
		for (int i = 0; i < jsonArray.size(); i++)
		{
			String string = jsonArray.getString(i);
			JSONArray array = JSONArray.parseArray(string);
			for (int j = 0; j < array.size(); j++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("applyno", applyObj.getFieldValue("no"));
				map.put("material_to_test", applyObj.getFieldValue("material_to_test"));
				map.put("bnumber", applyObj.getFieldValue("material_produtct_no"));
				map.putAll((Map<String, Object>) array.get(i));
				newDatas.add(map);
			}
		}
		List<Map> newList = DBUtil.bathInsertRecords(itemObj, newDatas, context.getUserName());
		newDatas = new ArrayList<Map>();
		for (Map map : newList)
		{
			USCObject resultObj = NewUSCObjectHelper.newObject(itemObj, map);
			newDatas.add(GetRelationData.getData(applyObj, resultObj));
		}
		if (!ObjectHelperUtils.isEmpty(DBUtil.bathInsertRecords(relationObj, newDatas, context.getUserName())))
		{
			applyObj.setFieldValue("completed", true);
			applyObj.setFieldValue("insor", context.getUserName());
			applyObj.setFieldValue("testtime", SystemTime.getTimestamp());
			applyObj.save(context);

		}
		return successfulOperation();
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
