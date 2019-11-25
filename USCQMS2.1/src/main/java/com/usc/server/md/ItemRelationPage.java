package com.usc.server.md;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ItemRelationPage implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 3518102973564367286L;
	private String id;
	private String no;
	private String name;
	private int defaultc;

	protected Map<String, ItemRelationPageSign> itemRelationPageSignMap;
	protected List<ItemRelationPageSign> itemRelationPageSignList;

	public ItemRelationPage()
	{
	}

	public String toString()
	{
		return this.no + "-" + this.name;
	}

}
