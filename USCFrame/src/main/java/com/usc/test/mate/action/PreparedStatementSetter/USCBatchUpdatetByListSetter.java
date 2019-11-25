package com.usc.test.mate.action.PreparedStatementSetter;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

/**
 * 
 * <p>
 * Title: USCBathUpdateByListPreparedStatementSetter
 * </p>
 * 
 * <p>
 * Description: JDBCTemplate批量插入Dto对象实现类
 * </p>
 * 
 * @author PuTianXiong
 * 
 * @date 2019年4月12日
 * 
 */
public class USCBatchUpdatetByListSetter implements BatchPreparedStatementSetter, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Timestamp time = new Timestamp(System.currentTimeMillis());
	private List<Map<String, Object>> list;
	private String muser;

	public USCBatchUpdatetByListSetter(List<Map<String, Object>> list, String muser)
	{
		this.list = list;
		this.muser = muser;
	}

	@Override
	public int getBatchSize()
	{
		return this.list != null ? this.list.size() : 0;
	}

	@Override
	public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException
	{
		ps.setObject(1, muser);
		ps.setObject(2, time);
		ps.setObject(3, list.get(i).get("id"));
	}

}
