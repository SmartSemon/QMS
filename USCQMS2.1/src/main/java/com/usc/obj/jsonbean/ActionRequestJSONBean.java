package com.usc.obj.jsonbean;

import java.util.List;

import com.usc.server.md.ItemGrid;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.ItemPage;
import com.usc.server.md.ItemRelationPage;
import com.usc.server.md.ModelRelationShip;
import com.usc.server.md.USCModelMate;

import lombok.Data;

@Data
public class ActionRequestJSONBean extends JSONBean
{
	private ItemInfo itemInfo;
	protected String implclass;

	public ActionRequestJSONBean()
	{

	}

	public void init()
	{
		if (this.itemInfo == null)
		{
			this.itemInfo = USCModelMate.getItemInfo(itemNo);
		}
	}

	@Override
	public ItemInfo getItemInfo(String itemNo)
	{
		return this.itemInfo;
	}

	@Override
	public ItemMenu getItemMenu(String menuNo)
	{
		return this.itemInfo.getItemMenu(menuNo);
	}

	@Override
	public List<ItemMenu> getItemMenus()
	{
		return this.itemInfo.getItemMenuList();
	}

	@Override
	public ItemPage getItemPage(String itemPropertyNo)
	{
		return this.itemInfo.getItemPage(itemPropertyNo);
	}

	@Override
	public ItemGrid getItemGrid(String itemGridNo)
	{
		return this.itemInfo.getItemGrid(itemGridNo);
	}

	@Override
	public ItemRelationPage getItemRelationPage(String itemRelationPageNo)
	{
		return this.itemInfo.getItemRelationPage(itemRelationPageNo);
	}

	@Override
	public ModelRelationShip getModelRelationShip(String relationShipNo)
	{
		return USCModelMate.getRelationShipInfo(relationShipNo);
	}

}
