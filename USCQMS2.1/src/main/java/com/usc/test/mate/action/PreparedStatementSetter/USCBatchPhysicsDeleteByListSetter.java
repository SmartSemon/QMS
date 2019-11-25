package com.usc.test.mate.action.PreparedStatementSetter;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

/**
 *
 * <p>
 * Title: USCBatchDeleteByListSetter
 * </p>
 *
 * <p>
 * Description: JDBCTemplate批量删除Dto对象实现类
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年4月15日
 *
 */
public class USCBatchPhysicsDeleteByListSetter implements BatchPreparedStatementSetter, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private List<Map> dtoList;

	public USCBatchPhysicsDeleteByListSetter(List<Map> list)
	{
		this.dtoList = list;

	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException
	{
		Map dto = dtoList.get(i);

		ps.setObject(1, dto.get("id"));
	}

	@Override
	public int getBatchSize()
	{
		return dtoList.size();
	}

}
