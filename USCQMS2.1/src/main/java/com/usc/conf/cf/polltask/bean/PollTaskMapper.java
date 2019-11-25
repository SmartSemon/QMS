package com.usc.conf.cf.polltask.bean;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public interface PollTaskMapper
{
	PollTaskBean getByImplclass(JdbcTemplate jdbcTemplate, String implclass);

	List<PollTaskBean> getAllNeedStartTask(JdbcTemplate jdbcTemplate);

	List<PollTaskBean> getAllTask(JdbcTemplate jdbcTemplate);
}
