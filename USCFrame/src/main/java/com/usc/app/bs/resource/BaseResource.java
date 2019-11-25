package com.usc.app.bs.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class BaseResource {

	protected static final String PARAMS = "queryParam";
	// jsonParams格式错误时可能抛出异常
	protected List<Map> parseRows(String jsonParams) throws Exception {
		JSONArray ja = JSONArray.parseArray(jsonParams);

		List<Map> rows = new ArrayList<Map>();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = null;
			jo = ja.getJSONObject(i);
			rows.add((Map)jo);
		}
		return rows;
	}

	protected Map parseRow(String jsonParam) throws Exception {
		return JSONObject.parseObject(jsonParam);
	}


}
