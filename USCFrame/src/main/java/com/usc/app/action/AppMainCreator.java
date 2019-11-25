package com.usc.app.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.usc.app.action.i.USCObjCreator;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.NewUSCObjectHelper;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.md.ItemField;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.USCModelMate;

public class AppMainCreator implements USCObjCreator
{
	private String itemNo = null;
	private Map<String, Object> initData = new HashMap<String, Object>();
	private boolean only = false;

	public AppMainCreator(String objType)
	{
		this.itemNo = objType;
	}

	@Override
	public USCObject create(InvokeContext context) throws Exception
	{

		if (!createAble(context))
		{
			return null;
		}
		HashMap<String, Object> data = (HashMap<String, Object>) DBUtil.insertRecord(this.itemNo, this.initData,
				context.getCurrUserName());
		return NewUSCObjectHelper.newObject(itemNo, data);
	}

	@Override
	public boolean createAble(InvokeContext context) throws Exception
	{
		if (itemNo == null)
		{
			this.itemNo = context.getItemNo();
		}
		this.initData = context.getInitData();
		if (this.itemNo == null || this.initData == null)
		{
			return false;
		}
		return true;
	}

	public boolean VerifyItemUniqueness(Map newData)
	{
		try
		{
			ItemInfo itemInfo = USCModelMate.getItemInfo(itemNo);
			setOnly(USCModelMate.VerifyItemUniqueness(itemInfo, newData));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return only;

	}

	public boolean isOnly()
	{
		return only;
	}

	public void setOnly(boolean only)
	{
		this.only = only;
	}

	@Override
	public String getOnlyFields(InvokeContext context) throws Exception
	{
		ItemInfo info = USCModelMate.getItemInfo(context.getItemNo());
		List<ItemField> fields = USCModelMate.getOnlyItemFields(info);
		if (fields == null)
		{
			return null;
		}
		StringBuffer fieldName = new StringBuffer("[ ");
		for (int i = 0; i < fields.size(); i++)
		{
			if (i > 0)
			{
				fieldName.append(",");
			}
			fieldName.append(fields.get(i).getName());
		}
		fieldName.append(" ]");
		return fieldName.toString();
	}

	public static List getItemOnlyFields(String itemNo) throws Exception
	{
		ItemInfo info = USCModelMate.getItemInfo(itemNo);
		List<ItemField> fields = USCModelMate.getOnlyItemFields(info);
		return fields;
	}

}
