package com.usc.test.mate.jsonbean;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.USCModelMate;

import lombok.Data;

@Data
public class USCObjectJSONBean
{
	@NotNull
	public String USERNAME;
	@NotNull
	public String ITEMNO;

	@NotNull
	public String ITEMGRIDNO;
	public String ITEMPROPERTYNO;
	public String ITEMRELATIONPAGENO;

	public String PAGEID;

	public String VIEWNO;

	public Integer FACETYPE;

//	@NotNull
	public Map DATA;

	public USCObjectJSONBean()
	{
		super();
	}

	public USCObjectJSONBean(String jsonString)
	{
		setFieldsValues(jsonString);
	}

	private void setFieldsValues(String jsonString)
	{

	}

	public USCObjectJSONBean(JSONObject jsonObject)
	{
		setFieldsValues(jsonObject);
	}

	private void setFieldsValues(JSONObject jsonObject)
	{

	}

	public ItemInfo getItemInfo()
	{
		return this.ITEMNO == null ? null : USCModelMate.getItemInfo(ITEMNO);
	}

}
