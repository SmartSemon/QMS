package com.usc.server.md;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ModelQueryView implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = -2073770527905585241L;
	protected String id;
	protected String no;
	protected String name;
	protected String itemNo;
	protected String wcondition;

	protected List<ItemMenu> itemMenus;
	protected ItemInfo itemInfo;
	protected ItemGrid itemGrid;
	protected ItemPage itemPropertyPage;

	public String toString()
	{
		return this == null ? null : this.no + "-" + this.name;

	}

}
