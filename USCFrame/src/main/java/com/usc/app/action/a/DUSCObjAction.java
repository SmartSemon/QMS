package com.usc.app.action.a;

import java.util.ArrayList;
import java.util.List;

import com.usc.app.action.i.DelObjectAction;
import com.usc.obj.api.USCObject;

public abstract class DUSCObjAction extends AbstractAction implements DelObjectAction
{
	private List<USCObject> deleteObjs = new ArrayList<USCObject>();

	public USCObject[] getDelObjs()
	{
		USCObject[] objects = new USCObject[deleteObjs.size()];
		this.deleteObjs.toArray(objects);
		return objects;
	}

	public void addDelObj(USCObject object)
	{
		deleteObjs.add(object);
	}
}
