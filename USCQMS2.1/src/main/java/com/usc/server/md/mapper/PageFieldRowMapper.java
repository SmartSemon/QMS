package com.usc.server.md.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.usc.server.md.ItemPageField;
import com.usc.server.util.BeanFactoryConverter;

public class PageFieldRowMapper implements RowMapper<ItemPageField>
{
	public PageFieldRowMapper()
	{
	}

	@Override
	public ItemPageField mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		ItemPageField info = null;
		try
		{
			info = BeanFactoryConverter.getBean(ItemPageField.class, rs);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return info;
	}

	protected Map<String, Object> createColumnMap(int columnCount)
	{
		return new LinkedCaseInsensitiveMap<>(columnCount);
	}

	protected String getColumnKey(String columnName)
	{
		return columnName;
	}

	@Nullable
	protected Object getColumnValue(ResultSet rs, int index) throws SQLException
	{
		return JdbcUtils.getResultSetValue(rs, index);
	}

}
