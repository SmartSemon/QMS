package com.usc.conf.cf.polltask.bean;

import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service("taskMapper")
public class PolllTaskMapperImpl implements PollTaskMapper, Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = -2012654636235479918L;

	@Override
	public PollTaskBean getByImplclass(JdbcTemplate jdbcTemplate, String implclass)
	{
		RowMapper<PollTaskBean> rm = BeanPropertyRowMapper.newInstance(PollTaskBean.class);
		PollTaskBean bean = jdbcTemplate.queryForObject(
				"SELECT name,implclass,isenable,polltime,outtime,selfstart FROM spolltask WHERE del=? AND implclass=?",
				new Object[]
				{ 0, implclass }, rm);
		return bean;
	}

	@Override
	public List<PollTaskBean> getAllNeedStartTask(JdbcTemplate jdbcTemplate)
	{
		RowMapper<PollTaskBean> rm = BeanPropertyRowMapper.newInstance(PollTaskBean.class);
		List<PollTaskBean> beans = jdbcTemplate.query(
				"SELECT name,implclass,isenable,polltime,outtime,selfstart FROM spolltask WHERE del=0 AND isenable=1",
				rm);
		return beans;
	}

	@Override
	public List<PollTaskBean> getAllTask(JdbcTemplate jdbcTemplate)
	{
		RowMapper<PollTaskBean> rm = BeanPropertyRowMapper.newInstance(PollTaskBean.class);
		List<PollTaskBean> beans = jdbcTemplate
				.query("SELECT name,implclass,isenable,polltime,outtime,selfstart FROM spolltask WHERE del=0", rm);
		return beans;
	}

}
