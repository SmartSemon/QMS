package com.usc.server.polltask;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.usc.conf.cf.polltask.bean.PollTaskBean;
import com.usc.conf.cf.polltask.server.PollTaskService;

@RestController
@RequestMapping(value = "/poll", produces = "application/json;charset=UTF-8")
public class PollTaskController
{
	@Autowired
	private PollTaskService pollTaskService;

	@PostMapping(value = "/taskList")
	public List<PollTaskBean> taskList()
	{
		return pollTaskService.taskList();
	}

	@PostMapping("/start")
	public Object startPollTask(HttpServletRequest request, HttpServletResponse response, @RequestBody String param)
			throws JsonParseException, JsonMappingException, IOException
	{
		JSONObject jsonObject = JSONObject.parseObject(param);

		return pollTaskService.start(jsonObject.getString("implclass"));
	}

	@PostMapping(value = "/stop")
	public Object stopPollTask(@RequestBody String param)
	{
		JSONObject jsonObject = JSONObject.parseObject(param);

		return pollTaskService.stop(jsonObject.getString("implclass"));
	}

	@PostMapping(value = "/restart")
	public Object restart(@RequestBody String param)
	{
		JSONObject jsonObject = JSONObject.parseObject(param);

		return pollTaskService.restart(jsonObject.getString("implclass"));
	}
}
