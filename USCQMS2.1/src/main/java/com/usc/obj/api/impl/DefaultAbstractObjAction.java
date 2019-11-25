package com.usc.obj.api.impl;

import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;

import com.usc.app.action.cache.OperationalCach;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.bean.ItemBean;
import com.usc.obj.api.impl.a.AbstractObjAction;
import com.usc.server.md.USCModelMate;
import com.usc.server.syslog.LOGActionEnum;

public class DefaultAbstractObjAction extends AbstractObjAction
{
	/**
	 *
	 */
	private static final long serialVersionUID = -5151262961715987526L;
	protected USCObject currObj = null;

	@Override
	public void setCurrObj(USCObject paramBCFObj)
	{
		this.currObj = paramBCFObj;
	}

	@Override
	public boolean delete(InvokeContext context) throws Exception
	{
		if (isDeleted(context))
		{
			return false;
		}
		if (!isDeleteAble(context))
		{
			return false;
		}
		boolean b = false;
		String objString = context.getItemNo();
		if (USCModelMate.containsObj(objString))
		{
			b = true;
			deleteDBData(context, this.currObj);
			if (b)
			{
				OperationalCach.putDeleteObj(this.currObj);

			}
		}
		return b;
	}

	@Async
	protected boolean deleteDBData(InvokeContext context, USCObject selectedObj)
	{
		ItemBean itemBean = USCServerBeanProvider.getItemBean();
		boolean b = false;
		try
		{
			String id = selectedObj.getID();
			b = itemBean.deleteItem(context.getItemNo(), id, context.getCurrUserName());
			if (b)
			{
				String objClass = this.currObj.getClass().getName();
				try
				{
					Class<?> clas = Class.forName(objClass);
					Constructor<?> constructor = clas.getConstructor(String.class, Map.class);
					Map<String, Object> delData = USCServerBeanProvider.getItemBean()
							.getRequestItemByID(this.currObj.getItemNo(), id);
					this.currObj = (USCObject) constructor.newInstance(this.currObj.getItemNo(), delData);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				OperationalCach.putDeleteObj(this.currObj);
				USCServerBeanProvider.getSystemLogger().writeDeleteItemLog((ApplicationContext) context, this.currObj,
						LOGActionEnum.DELETE);
			}

		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		return b;
	}

	@Override
	public boolean isDeleteAble(InvokeContext context) throws Exception
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
	public boolean isDeleted(InvokeContext context) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}
}
