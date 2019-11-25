package com.usc.obj.api.impl;

import java.util.HashMap;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.a.AbstractUSCObjectImpl;

/**
 *
 * <p>
 * Title: USCObjectImpl
 * </p>
 *
 * <p>
 * Description: USCObject实现类
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年5月7日
 *
 */
public class DefaultUSCObject extends AbstractUSCObjectImpl
{

	/**
	 *
	 */
	private static final long serialVersionUID = -4339072477679904075L;

	public DefaultUSCObject(String objType, HashMap<String, Object> map)

	{
		super(objType, map);
	}

	@Override
	public void setCurrObj(USCObject uscObject)
	{
		// TODO Auto-generated method stub

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

	@Override
	public boolean save(InvokeContext context)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDeleted(InvokeContext context) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}

}
