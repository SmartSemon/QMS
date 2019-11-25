package com.usc.app.action.a;

import java.util.HashMap;

import com.usc.app.action.i.ChildrenAction;
import com.usc.app.action.mate.MateFactory;
import com.usc.app.query.rt.QueryReturnRequest;
import com.usc.obj.api.USCClassAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.type.ClassNodeObject;
import com.usc.obj.jsonbean.JSONBean;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ItemPage;

public abstract class AbstractClassAction extends AbstractAction
		implements USCClassAction, QueryReturnRequest, ChildrenAction
{
	protected USCObject nodeObj = null;
	private ItemInfo classNodeItemInfo = null;

	private ItemInfo classItemInfo = null;
	private String classNodeItemNo = null;
	private ItemPage classNodeItemProperty = null;

	public ItemPage getClassNodeItemProperty()
	{
		return classNodeItemProperty;
	}

	public void setClassNodeItemProperty(ItemPage classNodeItemProperty)
	{
		this.classNodeItemProperty = classNodeItemProperty;
	}

	public void init(JSONBean jsonBean) throws Exception
	{
		this.classNodeItemNo = jsonBean.getClassNodeItemNo();
		String classNodeItemPropertyNo = jsonBean.getClassNodeItemPropertyNo();
		this.classNodeItemInfo = MateFactory.getItemInfo(classNodeItemNo);
		this.classNodeItemProperty = classNodeItemInfo.getItemPage(classNodeItemPropertyNo);

		String classItemNo = jsonBean.getClassItemNo();
		this.classItemInfo = MateFactory.getItemInfo(classItemNo);

		HashMap<String, Object> classNodeData = jsonBean.getClassNodeData();

		this.nodeObj = new ClassNodeObject(this.classNodeItemNo, classNodeData);

	}

	@Override
	public boolean hasChildren(USCObject object)
	{
		return false;
	}

	@Override
	public USCObject[] getChildren(USCObject object)
	{
		return null;
	}

	@Override
	public boolean disable() throws Exception
	{
		return false;
	}

	public USCObject getNodeObj()
	{
		return this.nodeObj;
	}

	public void setNodeObj(USCObject nodeObj)
	{
		this.nodeObj = nodeObj;
	}

	public void setClassItemInfo(ItemInfo classItemInfo)
	{
		this.classItemInfo = classItemInfo;
	}

	public ItemInfo getClassItemInfo()
	{
		return this.classItemInfo;
	}

	public String getClassNodeItemNo()
	{
		return classNodeItemNo;
	}

	public void setClassNodeItemNo(String classNodeItemNo)
	{
		this.classNodeItemNo = classNodeItemNo;
	}

	public ItemInfo getClassNodeItemInfo()
	{
		return classNodeItemInfo;
	}

	public void setClassNodeItemInfo(ItemInfo classNodeItemInfo)
	{
		this.classNodeItemInfo = classNodeItemInfo;
	}
}
