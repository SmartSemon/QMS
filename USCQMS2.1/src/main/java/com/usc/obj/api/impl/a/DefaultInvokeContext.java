package com.usc.obj.api.impl.a;

import java.util.HashMap;
import java.util.Map;

import com.usc.app.annotation.SystemLog;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.api.bean.AppMainCreator;
import com.usc.obj.api.bean.USCObjCreator;
import com.usc.obj.api.bean.UserInformation;

public abstract class DefaultInvokeContext implements InvokeContext
{

	protected USCObject[] objs = null;
	private Map initData = null;
	private Map extendInfo = new HashMap<String, Object>();
	protected String param = null;
	private String itemNo;
	private boolean isInitModel = true;
	private USCObjectAction objAction = null;

	public DefaultInvokeContext()
	{
	}

	public USCObject getSelectedObj()
	{
		return objs != null ? objs[0] : null;
	}

	public void setCurrObj(USCObject paramObj)
	{
		if (paramObj == null)
		{
			this.objs = null;
			return;
		}

		if (this.objs != null && this.objs.length > 0)
		{
			this.objs[0] = paramObj;
		} else
		{
			this.objs = new USCObject[]
			{ paramObj };
		}

	}

	public String getParam()
	{
		return this.param;
	}

	public void setParam(String paramString)
	{
		this.param = paramString;
	}

	public String getClientID()
	{
		UserInformation information = getUserInformation();
		if (information != null)
		{
			return information.getClientID();
		}
		return null;
	}

	public USCObject[] getSelectObjs()
	{
		// TODO Auto-generated method stub
		return objs;
	}

	public void setSelectObjs(USCObject[] objs)
	{

		this.objs = objs;
	}

	public boolean isInitModel()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void setInitModel(boolean paramBoolean)
	{
		this.isInitModel = paramBoolean;

	}

	public Map getInitData()
	{
		return this.initData;
	}

	public void setInitData(Map paramHashMap)
	{
		this.initData = paramHashMap;
	}

	public String getItemNo()
	{
		return itemNo;
	}

	public void setItemNo(String paramString)
	{
		this.itemNo = paramString;

	}

	public USCObjectAction getActionObjType()
	{
		return objAction;
	}

	public void setActionObjType(USCObjectAction objectAction)
	{
		this.objAction = objectAction;
	}

	public void putContext(InvokeContext context)
	{

	}

	public String getCurrUserName()
	{
		UserInformation userInformation = getUserInformation();
		if (userInformation != null)
		{
			return userInformation.getUserName();
		}
		return null;
	}

	public USCObject createObj(String objType) throws Exception
	{
		setItemNo(objType);
		USCObjCreator creator = new AppMainCreator(objType);
		if (((AppMainCreator) creator).VerifyItemUniqueness(this.getInitData()))
		{
			setExtendInfo("CreateResult",
					StandardResultTranslate.getResult("违反字段唯一性约束，字段：" + creator.getOnlyFields(this), false));
			return null;
		}
		USCObject object = creator.create(this);
		setCurrObj(object);
		return object;
	}

	@Override
	public Object getExtendInfo(Object paramObject)
	{
		return extendInfo.get(paramObject);
	}

	@Override
	public void setExtendInfo(Object paramObject1, Object paramObject2)
	{
		extendInfo.putIfAbsent(paramObject1, paramObject2);
	}

}
