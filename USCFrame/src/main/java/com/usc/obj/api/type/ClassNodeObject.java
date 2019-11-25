package com.usc.obj.api.type;

import java.util.HashMap;

public class ClassNodeObject extends GeneralObject
{

	/**
	 *
	 */
	private static final long serialVersionUID = -4649329598226055540L;

	public ClassNodeObject(String objType, HashMap<String, Object> map)
	{
		super(objType, map);
	}

	public String getBusinessObject()
	{
		return this.getFieldValueToString("itemno");
	}

}
