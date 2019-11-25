package com.usc.obj.api.type.relation;

import java.util.HashMap;

import com.usc.app.action.cache.OperationalCach;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.api.type.AbstractDBObj;

public class RelationShipObject extends AbstractDBObj
{

	/**
	 *
	 */
	private static final long serialVersionUID = -324707025588412961L;

	public RelationShipObject(String objType, HashMap<String, Object> map)
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

	public String toString()
	{
		return itemObjType;

	}

}
