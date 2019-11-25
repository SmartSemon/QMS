package com.usc.server.md;

import java.io.Serializable;
import java.util.Map;

import com.usc.server.util.BeanConverter;

import lombok.Data;

@Data
public class ItemMenu implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = -3816219930089042464L;
	private String id;
	private String no;
	private String name;
	private String mtype;
	private String implclass;
	private String webpath;
	private String icon;
	private String pid;
	private String param;
	private String reqparam;
	private String wtype;
	private String abtype;
	private String mno;

	private boolean disabled;

	public ItemMenu()
	{
	}

	public String toString()
	{
		return this.no + "-" + this.name;
	}

	public Map<String, Object> toMap()
	{
		ItemMenu menuType = this;
		return BeanConverter.toMap(menuType);
	}
}
