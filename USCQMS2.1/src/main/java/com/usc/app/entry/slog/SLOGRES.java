package com.usc.app.entry.slog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping(value = "/log", produces = "application/json;charset=UTF-8")
public class SLOGRES
{

	@GetMapping(value = "/details")
	public Object getDetails(@RequestParam String queryParam, HttpServletRequest request, HttpServletResponse response)
	{
		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		SLoggerUtil log = new SLoggerUtil(jsonObject, request, response);

		return log.getLogDetails();
	}
}
