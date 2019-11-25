package com.usc.test.mate.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.usc.app.bs.resource.BaseResource;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.dto.impl.MapDto;
import com.usc.server.jdbc.DBUtil;
import com.usc.test.mate.action.MCreateRelationAction;
import com.usc.test.mate.action.MRelationQueryAction;
import com.usc.test.mate.action.MUpOrDown;
import com.usc.test.mate.action.MUpdateAction;
import com.usc.test.mate.action.service.ModelItemServer;
import com.usc.test.mate.action.service.ModelServer;

@RestController
@RequestMapping(value = "/ModelItemRelationInfo", produces = "application/json;charset=UTF-8")
public class SysModelRelationInfoResource extends BaseResource
{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ModelServer modelServer;
	@Autowired
	private ModelItemServer modelItemServer;

	@Transactional
	@PostMapping("/createData")
	public Map<String, Object> createData(@RequestBody String queryParam)
	{
		return MCreateRelationAction.create(queryParam);
	}

	/**
	 * <p>
	 * 删除model_item 对象数据
	 *
	 * @param queryParam
	 * @return
	 */
	@Transactional
	@PostMapping("/delete")
	public Map<String, Object> delete(@RequestBody String queryParam)
	{
//		JSONObject jsonObject = JSONObject.parseObject(queryParam);
//		if (MDeleteAction.delete(jdbcTemplate, jsonObject))
//		{
//			return StandardResultTranslate.getResult(true, "Action_Delete");
//		}
//
//		return StandardResultTranslate.getResult(false, "Action_Delete");
		modelItemServer.deleteItem(queryParam);
		return new MapDto("flag", "sucess");
	}

	@Transactional
	@PostMapping("/update")
	public Map<String, Object> update(@RequestBody String queryParam)
	{
		try
		{
			return MUpdateAction.update(queryParam);
		} catch (Exception e)
		{
			e.printStackTrace();
			return StandardResultTranslate.getResult(false, "Action_Update" + "\n" + e.getMessage());
		}
	}

	@PostMapping("/updateDefaultc")
	public Map<String, Object> updateDefaultc(@RequestBody String queryParam)
	{
		try
		{
			return MUpdateAction.updateDefaultc(jdbcTemplate, queryParam);
		} catch (Exception e)
		{
			e.printStackTrace();
			return StandardResultTranslate.getResult(false, "Action_Update" + "\n" + e.getMessage());
		}
	}

	@PostMapping("/moveUpOrDown")
	public Map<String, Object> moveupordown(@RequestBody String queryParam)
	{
		try
		{
			return MUpOrDown.move(queryParam);
		} catch (Exception e)
		{
			e.printStackTrace();
			return StandardResultTranslate.getResult(false, "Action_Update" + "\n" + e.getMessage());
		}
	}

	@PostMapping("/moveMenu")
	public Map<String, Object> menumove(@RequestBody String queryParam)
	{
		try
		{
			return MUpOrDown.menuMove(queryParam);
		} catch (Exception e)
		{
			e.printStackTrace();
			return StandardResultTranslate.getResult(false, "Action_Update" + "\n" + e.getMessage());
		}
	}

	@GetMapping("/selectByCondition")
	public Object findModelFieldsPagingList(@RequestParam(value = "queryParam", required = false) String queryParam)
			throws JsonParseException, JsonMappingException, IOException
	{
		return MRelationQueryAction.query(queryParam);
	}

	@GetMapping("/getSysMenu")
	public Object getSysMenu(@RequestParam(value = "queryParam", required = false) String queryParam)
			throws JsonParseException, JsonMappingException, IOException
	{
		JSONObject jsonObject = JSONObject.parseObject(queryParam);

		String tableName = jsonObject.getString("tableName");
		Object[] objects = new Object[]
		{ 0 };
		List<Map<String, Object>> dataList = DBUtil.queryForList(tableName, "del=? ORDER BY sort", objects);
		return StandardResultTranslate.getQueryResult(true, "Action_Query", dataList);
	}

	@PostMapping("/createSysMenu")
	public Object add(@RequestBody String queryParam)
	{
		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		String tableName = jsonObject.getString("tableName");
		String user = jsonObject.getString("userName");
		Map data = JSONObject.parseObject(jsonObject.get("data").toString());
		data.remove("key");
		List list = new ArrayList<Map>();
		list.add(data);

		return MCreateRelationAction.create(tableName, null, null, user, list);
	}

}
