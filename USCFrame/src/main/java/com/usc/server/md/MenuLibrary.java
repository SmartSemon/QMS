package com.usc.server.md;

import java.io.Serializable;
import java.util.Map;

import com.usc.server.util.BeanConverter;

import lombok.Data;

@Data
public class MenuLibrary implements Serializable
{
	/**
	 *
	 */
	protected static final long serialVersionUID = -3816219930089042464L;
	protected String id;
	protected String no;
	protected String name;
	protected String mtype;
	protected String implclass;
	protected String webpath;
	protected String icon;
	protected String param;
	protected String reqparam;
	protected String wtype;
	protected String abtype;

	public MenuLibrary()
	{
	}

	public String toString()
	{
		return this.no + "-" + this.name;
	}

	public Map<String, Object> toMap()
	{
		MenuLibrary menuType = this;
		return BeanConverter.toMap(menuType);
	}
}
