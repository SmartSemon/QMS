package com.usc.obj.api.type.relation;

import java.util.HashMap;

import com.usc.app.action.cache.OperationalCach;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.api.type.AbstractDBObj;
import com.usc.server.md.field.FieldNameInitConst;

public class ClassRelationShipObject extends AbstractDBObj
{

	/**
	 *
	 */
	private static final long serialVersionUID = 3095406244771656993L;

	public ClassRelationShipObject(String objType, HashMap<String, Object> map)
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

	public String getDUser()
	{
		return (String) getFieldValueToString(FieldNameInitConst.FIELD_DUSER);
	}

	public String getOwner()
	{
		return (String) getFieldValueToString(FieldNameInitConst.FIELD_OWNER);
	}

	public String getMUser()
	{
		return (String) getFieldValueToString(FieldNameInitConst.FIELD_MUSER);
	}

	public Object getDeleteTime()
	{
		return getFieldValue(FieldNameInitConst.FIELD_DTIME);
	}

	public Object getMTime()
	{
		return getFieldValue(FieldNameInitConst.FIELD_MTIME);
	}

}
