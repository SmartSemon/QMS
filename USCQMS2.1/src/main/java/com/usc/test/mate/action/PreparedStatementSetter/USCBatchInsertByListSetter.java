package com.usc.test.mate.action.PreparedStatementSetter;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.usc.server.util.uuid.USCUUID;

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
public class USCBatchInsertByListSetter implements BatchPreparedStatementSetter, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Timestamp time = new Timestamp(System.currentTimeMillis());
	private List<Map> dtoList;
	private List<Map> resultList = new ArrayList<Map>();

	private Vector<String> ids = new Vector<>(); // 插入成功的IDS集合
	private String idsString = "";

	public USCBatchInsertByListSetter(List<Map> dtoList)
	{
		this.dtoList = dtoList;
	}

	@Override
	public int getBatchSize()
	{
		if (this.dtoList != null)
		{
			return this.dtoList.size();
		}
		return 0;
	}

	@Override
	public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException
	{
		Map dto = dtoList.get(i);
		String id = USCUUID.UUID();
		dto.put("id", id);
		dto.put("del", 0);
		dto.put("ctime", time);
		dto.put("state", "C");
		dto.put("cuser", "admin");
		int j = 1;

		for (Object field : dto.keySet())
		{
			Object object = dto.get(field);
			ps.setObject(j++, object);
		}
		resultList.add(dto);
		ids.add(id);
		idsString += id + ",";
	}

	public Vector<String> getIdsVector()
	{
		return this.ids;
	}

	public String getIdsString()
	{
		return this.idsString.substring(0, this.idsString.length() - 1);
	}

	public List<Map> getResultList()
	{
		return resultList;
	}

	public void setResultList(List<Map> resultList)
	{
		this.resultList = resultList;
	}
}
