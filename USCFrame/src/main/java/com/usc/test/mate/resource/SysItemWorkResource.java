package com.usc.test.mate.resource;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;
import com.usc.server.jdbc.DBUtil;
import com.usc.test.mate.action.MCreateRelationAction;

@RestController
@RequestMapping(value = "/sysItemWork", produces = "application/json;charset=UTF-8")
public class SysItemWorkResource
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping("/create")
	public Map<String, Object> add(@RequestBody String queryParam)
	{
		if (queryParam == null)
			return null;
		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		String user = jsonObject.getString("userName");
		String table = jsonObject.getString("tableName");
		Object dataString = jsonObject.get("data");

		if (dataString == null)
			return null;

		JSONArray jsonArray = JSONArray.parseArray(String.valueOf(dataString));
		List<Map> list = JSONArray.parseArray(dataString.toString(), Map.class);

		MCreateRelationAction.create(table, null, null, user, list);

		return StandardResultTranslate.getResult("Action_Create", list);
	}

	@PostMapping("/delete")
	public Dto delete(@RequestBody String queryParam)
	{
		if (queryParam == null || queryParam.trim().equals("{}"))
			return null;

//		USCModelMate.getItemInfoBean(jdbcTemplate, "TEST1");
		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		Object table = jsonObject.get("tableName");

		List<Map> list = JSONArray.parseArray(String.valueOf(jsonObject.get("data")), Map.class);
		String del = "UPDATE " + table + " SET del=1,state='HS',duser=?,dtime=? WHERE id=?";
		jdbcTemplate.batchUpdate(del, new BatchPreparedStatementSetter()
		{
			private final Timestamp dtime = new Timestamp(System.currentTimeMillis());

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException
			{
				Map<String, Object> map = list.get(i);
				ps.setObject(1, jsonObject.get("userName"));
				ps.setObject(2, dtime);
				ps.setObject(3, map.get("id"));

			}

			@Override
			public int getBatchSize()
			{
				return list.size();
			}
		});
		return new MapDto("flag", true);
	}

	@PostMapping("/update")
	public Dto update(@RequestBody String queryParam)
	{
		if (queryParam == null || queryParam.trim().equals("{}"))
			return null;

		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		Object table = jsonObject.get("tableName");
		String uData = jsonObject.getString("uData");
		if (uData == null || jsonObject.getString("uData").trim().equals("{}"))
			return new MapDto("flag", false);
		String setSql = "";
		JSONObject jsonObjectU = JSONObject.parseObject(uData);
		Set<String> setKeys = jsonObjectU.keySet();
		for (String string : setKeys)
		{
			setSql += string + "='" + jsonObjectU.get(string) + "',";
		}
		List<Map> list = JSONArray.parseArray(String.valueOf(jsonObject.get("data")), Map.class);
		String del = "UPDATE " + table + " SET " + setSql + "muser='" + jsonObject.get("userName")
				+ "',mtime=? WHERE id=?";
		jdbcTemplate.batchUpdate(del, new BatchPreparedStatementSetter()
		{
			private final Timestamp mtime = new Timestamp(System.currentTimeMillis());

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException
			{
				Map<String, Object> map = list.get(i);
				ps.setObject(1, mtime);
				ps.setObject(2, map.get("id"));

			}

			@Override
			public int getBatchSize()
			{
				return list.size();
			}
		});
		return new MapDto("flag", true);
	}

	@GetMapping("/packet")
	public Object findPagingList(@RequestParam(value = "queryParam", required = false) String queryParam)
			throws JsonParseException, JsonMappingException, IOException
	{
		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		String table = jsonObject.getString("tableName");
		String conString = jsonObject.getString("condition");
		StringBuffer condition = new StringBuffer("del=?");
		if (conString != null && !"".equals(conString))
		{
			condition.append(" AND " + conString);
		}
		List list = DBUtil.queryForList(table, condition.toString(), 0);
		return StandardResultTranslate.getQueryResult(true, "Action_Query", list);
	}

}
