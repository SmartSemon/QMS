package com.usc.app.action.a;

import java.util.Map;

import com.usc.app.action.i.RelationAction;
import com.usc.app.action.mate.MateFactory;
import com.usc.app.query.rt.QueryReturnRequest;
import com.usc.obj.api.USCObject;
import com.usc.obj.jsonbean.JSONBean;
import com.usc.obj.util.NewUSCObjectHelper;
import com.usc.server.md.ModelQueryView;
import com.usc.server.md.ModelRelationShip;
import com.usc.server.md.USCModelMate;

import lombok.Data;

@Data
public abstract class AbstractRelationAction extends AbstractAction implements RelationAction, QueryReturnRequest
{
	protected USCObject root = null;
	private ModelRelationShip relationShip = null;
	private ModelQueryView relationQueryView = null;

	public void init(JSONBean jsonBean)
	{
		String relationShipNo = jsonBean.getRelationShipNo();
		String relationQueryViewNo = jsonBean.getQueryViewNo();
		String itemA = jsonBean.getItemA();
		Map itemAData = jsonBean.getItemAData();
		if (relationShipNo != null)
		{
			setRelationShip(USCModelMate.getRelationShipInfo(relationShipNo));
		}
		if (relationQueryViewNo != null)
		{
			setRelationQueryView(MateFactory.getQueryView(relationQueryViewNo));
		}
		setRoot(NewUSCObjectHelper.newObject(itemA, itemAData));
	}

	public USCObject getRoot()
	{
		if (this.root != null)
		{
			return this.root;
		}
		Map map = (Map) context.getExtendInfo("itemAData");

		this.root = NewUSCObjectHelper.newObject(relationShip.getItemA(), map);
		return getRoot();
	}

	public void setRoot(USCObject root)
	{
		this.root = root;
	}

	public boolean checkMaxShipNum()
	{
		return true;
	}

	@Override
	public boolean disable() throws Exception
	{
		if (!checkMaxShipNum())
		{
			return Boolean.FALSE;
		}
		return true;
	}

}
