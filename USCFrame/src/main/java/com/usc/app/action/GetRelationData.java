package com.usc.app.action;

import java.util.HashMap;
import java.util.Map;

import com.usc.obj.api.USCObject;

public class GetRelationData
{
	public static Map<String, Object> getData(USCObject root, USCObject newObj)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itema", root.getItemNo());
		map.put("itemaid", root.getID());
		map.put("itemb", newObj.getItemNo());
		map.put("itembid", newObj.getID());
		return map;
	}
}
