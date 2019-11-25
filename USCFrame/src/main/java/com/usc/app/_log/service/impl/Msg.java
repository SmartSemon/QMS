package com.usc.app._log.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class Msg
{

	private String code;
	private String msg;
	private Map[] dataList;
	private Object token;

	public Msg(String code, String msg)
	{
		this(code, msg, null, null);
	}

	public Msg(String code, String msg, Object token, Map... maps)
	{
		this.code = code;
		this.msg = msg;
		this.token = token;
		this.dataList = maps;
	}

	public Map<String, Object> toMap()
	{
		Map<String, Object> rtMsg = new HashMap<String, Object>();
		rtMsg.put("code", this.code);
		rtMsg.put("msg", this.msg);
		rtMsg.put("token", this.token);
		rtMsg.put("dataList", dataList);
		return rtMsg;
	}

	public String toJsonString()
	{
		Map<String, Object> dto = this.toMap();
		JSONObject json = new JSONObject(dto);
		return json.toString();
	}

}
