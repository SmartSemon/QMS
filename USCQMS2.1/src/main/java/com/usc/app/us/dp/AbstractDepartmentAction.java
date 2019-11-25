package com.usc.app.us.dp;

import java.util.HashMap;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.action.i.ChildrenAction;
import com.usc.app.query.rt.QueryReturnRequest;
import com.usc.obj.api.USCClassAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.type.DepartMentObject;
import com.usc.obj.jsonbean.JSONBean;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ItemPage;
import com.usc.server.md.USCModelMate;

import lombok.Data;

@Data
public abstract class AbstractDepartmentAction extends AbstractAction
		implements USCClassAction, QueryReturnRequest, ChildrenAction
{
	protected USCObject departMent = null;

	private ItemInfo departMentInfo = null;

	private ItemPage departMentProperty = null;

	public ItemPage getClassNodeItemProperty()
	{
		return departMentProperty;
	}

	public void setClassNodeItemProperty(ItemPage classNodeItemProperty)
	{
		this.departMentProperty = classNodeItemProperty;
	}

	public void init(JSONBean jsonBean) throws Exception
	{
		String classNodeItemNo = jsonBean.getClassNodeItemNo();
		String classNodeItemPropertyNo = jsonBean.getClassNodeItemPropertyNo();
		this.departMentInfo = USCModelMate.getItemInfo(classNodeItemNo);
		this.departMentProperty = departMentInfo.getItemPage(classNodeItemPropertyNo);

		HashMap<String, Object> classNodeData = jsonBean.getClassNodeData();

		this.departMent = new DepartMentObject(classNodeItemNo, classNodeData);

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
		return true;
	}

}
