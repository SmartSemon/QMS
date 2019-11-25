package com.usc.obj.jsonbean;

import java.util.List;

import com.usc.server.md.ItemGrid;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.ItemPage;
import com.usc.server.md.ItemRelationPage;
import com.usc.server.md.ModelRelationShip;

public interface ResourceModelComponent
{
	public abstract ItemInfo getItemInfo(String itemNo);

	public abstract ItemMenu getItemMenu(String menuNo);

	public abstract List<ItemMenu> getItemMenus();

	public abstract ItemPage getItemPage(String itemPropertyNo);

	public abstract ItemGrid getItemGrid(String itemGridNo);

	public abstract ItemRelationPage getItemRelationPage(String itemRelationPageNo);

	public abstract ModelRelationShip getModelRelationShip(String relationShipNo);
}
