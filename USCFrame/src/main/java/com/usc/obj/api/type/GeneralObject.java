package com.usc.obj.api.type;

import java.util.Date;
import java.util.HashMap;

import com.usc.app.action.cache.OperationalCach;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObjectAction;
import com.usc.server.md.field.FieldNameInitConst;

public class GeneralObject extends AbstractDBObj
{

	/**
	 *
	 */
	private static final long serialVersionUID = 2222483531816988022L;

	public GeneralObject(String objType, HashMap<String, Object> map)
	{
		super(objType, map);
	}

	@Override
	public boolean delete(InvokeContext context) throws Exception
	{
		if (!isDeleteAble(context))
		{
			return false;
		}
		boolean b = false;
		if (!isDeleted(context))
		{
			USCObjectAction objectAction = context.getActionObjType();
			objectAction.setCurrObj(this);
			b = objectAction.delete(context);
			if (b)
			{
				setObj2Deleted();
				OperationalCach.putDeleteObj(this);
			}
		}

		return b;
	}

	public String getOwner()
	{
		return getFieldValueToString(FieldNameInitConst.FIELD_OWNER);
	}

	public String getCreateUser()
	{
		return getFieldValueToString(FieldNameInitConst.FIELD_CUSER);
	}

	public Date getCreateTime()
	{
		return (Date) getFieldValue(FieldNameInitConst.FIELD_CTIME);
	}

	public String getDeleteUser()
	{
		return getFieldValueToString(FieldNameInitConst.FIELD_DUSER);
	}

	public Date getDeleteTime()
	{
		return getFieldValueToDate(FieldNameInitConst.FIELD_DTIME);
	}

	public String getModifiyUser()
	{
		return getFieldValueToString(FieldNameInitConst.FIELD_MUSER);
	}

	public Date getModifiyTime()
	{
		return getFieldValueToDate(FieldNameInitConst.FIELD_MTIME);
	}

	public Object getState()
	{
		return getFieldValue(FieldNameInitConst.FIELD_STATE);
	}

	public String getNo()
	{
		return getFieldValueToString(FieldNameInitConst.FIELD_NO);
	}

	public String getName()
	{
		return getFieldValueToString(FieldNameInitConst.FIELD_NAME);
	}

	public void setNo(String paramString)
	{
		setFieldValue(FieldNameInitConst.FIELD_NO, paramString);
	}

	public void setName(String paramString)
	{
		setFieldValue(FieldNameInitConst.FIELD_NAME, paramString);
	}
}
