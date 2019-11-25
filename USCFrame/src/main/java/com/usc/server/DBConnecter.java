package com.usc.server;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.usc.util.SpringContextUtil;

public class DBConnecter implements Serializable
{

	protected static final long serialVersionUID = 1L;
	static InitialContext context = null;

	private static JdbcTemplate jdbcTemplate;
	private static DataSource dataSource;

	public static String JNDI = "USCDBS";

	public static JdbcTemplate getJdbcTemplate()
	{
		if (jdbcTemplate == null)
		{
			try
			{
				jdbcTemplate = new JdbcTemplate(getDataSource());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return jdbcTemplate;
	}

	public static void setJdbcTemplate(JdbcTemplate jdbcTemp)
	{
		jdbcTemplate = jdbcTemp;
	}

	public static String getJNDI()
	{
		return JNDI;
	}

	public static Connection getConnection() throws Exception
	{

		return getConnection(getJNDI());
	}

	public static DataSource getDataSource() throws Exception
	{

		if (dataSource == null)
		{
//			dataSource = (DataSource) context.lookup(getJNDI());
//			if (dataSource == null)
//			{
//				dataSource = SpringContextUtil.getBean(DruidDataSource.class);
//			}
			dataSource = SpringContextUtil.getBean(DruidDataSource.class);
		}
		return dataSource;
	}

	private static Connection getConnection(String jndi2)
	{
//			if (context == null)
//			{
//				context = new InitialContext();
//			}
//			DataSource dataSource = SpringContextUtil.getBean(DruidDataSource.class);
//			if (dataSource == null)
//			{
//				dataSource = (DataSource) context.lookup(jndi2);
//			}

		try
		{
			return getDataSource().getConnection();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String getDataBaseProductName()
	{
		try
		{
			return DBConnecter.getDataSource().getConnection().getMetaData().getDatabaseProductName();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isOracle()
	{
		return "Oracle".equals(getDataBaseProductName());
	}

	public static boolean isMySQL()
	{
		return "MySQL".equals(getDataBaseProductName());
	}
}
