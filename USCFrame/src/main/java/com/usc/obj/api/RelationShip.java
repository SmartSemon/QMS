package com.usc.obj.api;

import java.util.List;

import com.usc.server.md.ItemMenu;
import com.usc.server.md.ModelRelationShip;

public abstract interface RelationShip
{
	public abstract ModelRelationShip getRelationShip();

	public abstract List<ItemMenu> getItemMenus();
}
