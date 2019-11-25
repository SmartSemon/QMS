package com.usc.server.md;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.usc.server.util.BeanConverter;

import lombok.Data;

@Data
public class ItemGrid implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = -9148923512210049627L;
	private String id;
	private String no;
	private String name;
	private String align;
	private int defaultc;
	private int type;

	protected List<ItemGridField> gridFieldList;
	protected Map<String, ItemGridField> gridFieldMap;

	public ItemGridField getItemGridField(String no)
	{
		return this == null ? null : (this.gridFieldMap == null) ? null : this.gridFieldMap.get(no);
	}

	public String toString()
	{
		return this == null ? null : this.no + "-" + this.name;
	}

	public Map<String, Object> toMap()
	{
		ItemGrid gridType = this;
		return BeanConverter.toMap(gridType);

	}

}
