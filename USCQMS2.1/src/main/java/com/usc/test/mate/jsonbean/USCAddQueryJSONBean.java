package com.usc.test.mate.jsonbean;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class USCAddQueryJSONBean extends USCRelationObjectJSONBean
{
	public String QUERYWORD;
	public List<Map> RDATA;
	public int PAGE;
}
