package com.usc.obj.api.impl;

import java.util.HashMap;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObject;

public class USCRelationObj extends DefaultUSCObject
{
	/**
	 *
	 */
	private static final long serialVersionUID = -117562028428292131L;
	protected USCObject itemA = null;
	protected USCObject itemB = null;

	public USCRelationObj(String objType, HashMap<String, Object> map)
	{
		super(objType, map);
	}

	public USCObject getItemA()
	{
		if (this.itemA == null)
		{
			String ta = getItemATable();
			String taID = getItemAID();
			this.itemA = USCObjectFactory.createObj(ta, taID);
		}
		return itemA;
	}

	public USCObject getItemB()
	{
		return null;
	}

	private String getItemAID()
	{
		return null;
	}

	private String getItemATable()
	{
		return null;
	}

	private String getItemBID()
	{
		return null;
	}

	private String getItemBTable()
	{
		return null;
	}

	@Override
	public boolean isDeleteAble(InvokeContext context) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(InvokeContext context) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canClone(InvokeContext context) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public USCObject cloneObj(InvokeContext context) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDisplayable(InvokeContext paramInvokeContext)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
