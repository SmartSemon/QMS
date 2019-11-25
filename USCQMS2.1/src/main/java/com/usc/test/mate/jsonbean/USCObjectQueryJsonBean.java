package com.usc.test.mate.jsonbean;

import com.usc.server.md.ItemInfo;
import com.usc.server.md.USCModelMate;

import lombok.Data;

@Data
public class USCObjectQueryJsonBean extends USCObjectJSONBean
{

	private String QUERYWORD;

	public String CONDITION;

	public Integer PAGE;

	private String classNodeItemNo;
	private String classNodeItemPropertyNo;
	private String classItemNo;

	public USCObjectQueryJsonBean()
	{
		super();
	}

	public ItemInfo getItemInfo()
	{
		return USCModelMate.getItemInfo(ITEMNO);
	}

}
