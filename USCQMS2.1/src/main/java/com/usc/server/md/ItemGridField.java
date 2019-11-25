package com.usc.server.md;

import java.io.Serializable;

import lombok.Data;

@Data
public class ItemGridField implements Serializable
{
	/**
	 *
	 */
	protected static final long serialVersionUID = -9148923512210049627L;
	protected String id;
	protected String no;
	protected String fieldName;
	protected String name;
	protected String FType;
	protected int FLength;
	protected int allowNull;
	protected int accuracy;
	protected int only;
	protected String defaultV;
	protected String remark;
	protected int type;
	protected String editor;
	protected String editParams;
	protected String editAble;
	protected Double width;
	protected String align;
	protected int screen;
	protected int sort;

	public String toString()
	{
		return this == null ? this.getClass().getSimpleName() : this.no + "-" + this.name;
	}
}
