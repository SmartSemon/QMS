package com.usc.app.bs.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.usc.app.bs.page.PageInfo;
import com.usc.dto.Dto;


/**
 *<p>增删改查顶级接口</p>
 *
 */
public interface DaoService {

	/**
	 * @describe 逐条插入
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void insert(Dto parameter) throws DataAccessException;
	public void insert(String statement, Dto parameter) throws DataAccessException;

	/**
	 * @describe 批量插入
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void insert(List<Dto> parameter) throws DataAccessException;
	public void insert(String statement, List<Dto> parameter) throws DataAccessException;

	/**
	 * @describe 物理删除
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void delete(Dto parameter) throws DataAccessException;
	public void delete(String statement, Dto parameter) throws DataAccessException;

	/**
	 * @describe 批量物理删除
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void delete(List<Dto> parameter) throws DataAccessException;
	public void delete(String statement, List<Dto> parameter) throws DataAccessException;

	/**
	 * @describe 逻辑删除
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void logicDelete(Dto parameter) throws DataAccessException;
	public void logicDelete(String statement, Dto parameter) throws DataAccessException;

	/**
	 * @describe 批量逻辑删除
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void logicDelete(List<Dto> parameter) throws DataAccessException;
	public void logicDelete(String statement, List<Dto> parameter) throws DataAccessException;

	/**
	 * @describe 逐条更新
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void update(Dto parameter) throws DataAccessException;
	public void update(String statement, Dto parameter) throws DataAccessException;

	/**
	 * @describe 批量更新
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void update(List<Dto> parameter) throws DataAccessException;
	public void update(String statement, List<Dto> parameter) throws DataAccessException;

	/**
	 * @describe 根据主键查询数据
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public <T> T selectOne(Dto parameter);
	public <T> T selectOne(String statement, Dto parameter);

	/**
	 * @describe 根据条件查询数据
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public <T> List<T> selectList(Dto parameter);
	public <T> List<T> selectList(String statement, Dto parameter);
	/** 用于模糊查询
	 * e yds
	 * @param parameter
	 * @return
	 */
	public <T> List<T> selectLikeList(Dto parameter);

	/**
	 * @describe 分页查询
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public <T> PageInfo<T> findPagingList(Dto parameter);
	/**yds
	 *  e 用于模糊分页查询
	 * @param parameter
	 * @return
	 */
	public <T> PageInfo<T> likePagingList(Dto parameter);


	/**
	 * yds 将数据导入数据库
	 *
	 * @param file      传过来的流数据
	 * @param selectOne 关联表的数据
	 * @param fileName  导入文件的名字
	 * @return
	 * @throws Exception
	 */
	public List<Dto> importExcel(InputStream file, String selectOne,String fileName,Dto param) throws Exception;

	/**
	 * yds 将数据导出到模板
	 *
	 * @param selectOne     关联表的数据
	 * @param param         查询的条件
	 * @param copyExcelName 复制模板后的名字
	 * @return
	 */
	public Boolean exportExcel(String selectOne, List<Dto> list, String copyExcelName);


}












