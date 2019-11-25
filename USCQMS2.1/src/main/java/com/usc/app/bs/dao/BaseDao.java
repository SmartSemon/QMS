package com.usc.app.bs.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface BaseDao {

	/**
	 * @describe 插入数据
	 * @author caochengde
	 * @data 2018年11月20日
	 *
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DataAccessException
	 */
	public int insert(String statement, Object parameter) throws DataAccessException;

	/**
	 * @describe 删除数据
	 * @author caochengde
	 * @data 2018年11月20日
	 *
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DataAccessException
	 */
	public int delete(String statement, Object parameter) throws DataAccessException;

	/**
	 * @describe 修改数据
	 * @author caochengde
	 * @data 2018年11月20日
	 *
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DataAccessException
	 */
	public int update(String statement, Object parameter) throws DataAccessException;

	/**
	 * @describe 查询一条数据
	 * @author caochengde
	 * @data 2018年11月20日
	 *
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T selectOne(String statement, Object parameter) throws DataAccessException;

	/**
	 * @describe 查询指定数据表的所有数据
	 * @author caochengde
	 * @data 2018年11月20日
	 *
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> selectList(String statement, Object parameter) throws DataAccessException;

	/**
	 * @describe 分页查询数据
	 * @author putianxiong
	 * @param table
	 * @param condition
	 * @param orderFields
	 * @param limit
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> queryListDto(String table,String condition, String orderFields ,String limit) throws DataAccessException;

}





