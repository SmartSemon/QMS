package com.usc.server.md;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.usc.app.action.mate.MateFactory;

import lombok.Data;

@Data
public class ModelRelationShip implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1723137020779401634L;

	protected String id;
	protected String no;
	protected String name;
	protected String relationItem;
	protected String itemA;
	protected String itemB;
	protected String pItem;
	protected String param;
	protected String ship;

	protected List<ItemMenu> relationMenuList = null;
	protected Map<String, ItemMenu> relationMenuMap = null;

	protected List<ItemMenu> itemMenus;
	protected ItemInfo itemInfo;
	protected ItemGrid itemGrid;
	protected ItemPage itemPropertyPage;
	protected List<ItemRelationPageSign> itemRelationPage;

	public String getrelationTableName()
	{
		if (relationItem != null)
		{
			return MateFactory.getItemInfo(this.relationItem).getTableName();
		}
		return null;
	}

	public String toString()
	{
		return this == null ? null : this.no + "-" + this.name;

	}

}
