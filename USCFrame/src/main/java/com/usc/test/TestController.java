package com.usc.test;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/hello", produces = "application/json;charset=UTF-8")
	public Object name()
	{
		String sql = "SELECT * FROM product where del=0";

		List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
		return maps;

	}

}
