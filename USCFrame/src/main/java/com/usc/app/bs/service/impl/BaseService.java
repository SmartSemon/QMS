package com.usc.app.bs.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usc.app.bs.annotation.MapperName;
import com.usc.app.bs.dao.BaseDao;
import com.usc.app.util.CookieUtils;
import com.usc.dto.Dto;


/**
 * @describe Service基类
 * @author caochengde
 * @data 2018年10月29日
 *
 */
public abstract class BaseService {

	/**
	 * @describe mapper.xml文件中sql语句的id
	 * @author caochengde
	 * @data 2018年10月30日
	 */
	protected static interface MAPPER {
		String INSERT = "INSERT";// 逐条插入数据
		String BATCH_INSERT = "BATCH_INSERT";// 批量插入数据
		String DELETE = "DELETE";// 物理删除
		String BATCH_DELETE = "BATCH_DELETE";// 批量物理删除
		String UPDATE_DELETE = "UPDATE_DELETE";// 逻辑删除，修改delete字段
		String BATCH_UPDATE_DELETE = "BATCH_UPDATE_DELETE";// 逻辑删除，修改delete字段
		String UPDATE = "UPDATE";// 根据主键修改数据
		String BATCH_UPDATE = "BATCH_UPDATE";// 批量修改数据
		String FIND = "FIND";// 根据主键查询数据
		String FIND_LIST = "FIND_LIST";// 根据条件查询数据
		String LIKE_LIST = "LIKE_LIST";// 模糊查询

		/**
		 * @describe 字段
		 */
		static interface field {
			String createTime = "createTime";// 创建时间
			String updateTime = "updateTime";// 修改时间
			String WHSJ = "WHSJ";	//维护时间
			String GDRQ = "GDRQ";	//归档日期
			String RKRQ = "RKRQ";
			String WHR  = "WHR";//维护人
		}
	}

	@Autowired
	private BaseDao baseDao;

	@Autowired(required=false)
	private HttpServletRequest request;

	public BaseDao getDao() {
		return baseDao;
	}

	/**
	 * @describe 解析MapperName注解，获取value作为mapper的命名空间
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @return
	 */
	protected String getNamespace() {
		MapperName mapperName = getClass().getAnnotation(MapperName.class);
		String nameSpace = mapperName == null ? "" : mapperName.value() + ".";
		if (nameSpace == "") {
			System.err.println(String.format("Service [%s] 未定义@MapperName注解！", getClass().getName()));
		}
		return nameSpace;
	}

	/**
	 * @describe 逐条插入，使用给定的参数对象执行INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void insert(Dto parameter) throws DataAccessException {
		//c 添加创建时间
		parameter.put(MAPPER.field.createTime, new Date());
		parameter.put(MAPPER.field.WHSJ, new Date());
		parameter.put(MAPPER.field.GDRQ, new Date());
		parameter.put(MAPPER.field.RKRQ, new Date());
		parameter.put(MAPPER.field.WHR, CookieUtils.getCookie(request,"login_name"));
		insert(MAPPER.INSERT, parameter);
	}

	/**
	 * @describe 逐条插入，使用给定的参数对象执行特定的INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void insert(String statement, Dto parameter) throws DataAccessException {
		getDao().insert(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 批量插入，使用给定的参数对象执行INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void insert(List<Dto> parameter) throws DataAccessException {
		//c 添加创建时间
		for(Dto d:parameter) {
			d.put(MAPPER.field.createTime, new Date());
			d.put(MAPPER.field.WHSJ, new Date());
			d.put(MAPPER.field.GDRQ, new Date());
			d.put(MAPPER.field.RKRQ, new Date());
			d.put(MAPPER.field.WHR, CookieUtils.getCookie(request,"login_name"));

		}
//		System.err.println(parameter);
		insert(MAPPER.BATCH_INSERT, parameter);
	}

	/**
	 * @describe 批量插入，使用给定的参数对象执行特定的INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void insert(String statement, List<Dto> parameter) throws DataAccessException {
		getDao().insert(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 物理删除，使用给定的参数对象执行INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void delete(Dto parameter) throws DataAccessException {
		delete(MAPPER.DELETE, parameter);
	}

	/**
	 * @describe 物理删除，使用给定的参数对象执行特定的INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void delete(String statement, Dto parameter) throws DataAccessException {
		getDao().delete(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 批量物理删除，使用给定的参数对象执行INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void delete(List<Dto> parameter) throws DataAccessException {
		delete(MAPPER.BATCH_DELETE, parameter);
	}

	/**
	 * @describe 批量物理删除，使用给定的参数对象执行特定的INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void delete(String statement, List<Dto> parameter) throws DataAccessException {
		getDao().delete(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 逻辑删除，使用给定的参数对象执行INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void logicDelete(Dto parameter) throws DataAccessException {
		parameter.put(MAPPER.field.WHR, CookieUtils.getCookie(request,"login_name"));
		logicDelete(MAPPER.UPDATE_DELETE, parameter);
	}

	/**
	 * @describe 逻辑删除，使用给定的参数对象执行特定的INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void logicDelete(String statement, Dto parameter) throws DataAccessException {
		getDao().delete(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 批量逻辑删除，使用给定的参数对象执行INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void logicDelete(List<Dto> parameter) throws DataAccessException {
		for (Dto dto : parameter) {
			dto.put(MAPPER.field.WHR, CookieUtils.getCookie(request,"login_name"));
		}
		logicDelete(MAPPER.BATCH_UPDATE_DELETE, parameter);
	}

	/**
	 * @describe 批量逻辑删除，使用给定的参数对象执行特定的INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void logicDelete(String statement, List<Dto> parameter) throws DataAccessException {
		getDao().delete(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 根据主键修改数据，使用给定的参数对象执行INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void update(Dto parameter) throws DataAccessException {
		//c 添加修改时间
		parameter.put(MAPPER.field.updateTime, new Date());
		parameter.put(MAPPER.field.WHSJ, new Date());
		parameter.put(MAPPER.field.GDRQ, new Date());
		parameter.put(MAPPER.field.WHR, CookieUtils.getCookie(request,"login_name"));
		update(MAPPER.UPDATE, parameter);
	}

	/**
	 * @describe 根据主键修改数据，使用给定的参数对象执行特定的INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void update(String statement, Dto parameter) throws DataAccessException {
		getDao().update(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 批量修改数据，使用给定的参数对象执行INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void update(List<Dto> parameter) throws DataAccessException {
		//c 添加修改时间
		for(Dto d:parameter) {
			d.put(MAPPER.field.updateTime, new Date());
			d.put(MAPPER.field.WHSJ, new Date());
			d.put(MAPPER.field.GDRQ, new Date());
			d.put(MAPPER.field.WHR, CookieUtils.getCookie(request,"login_name"));
		}
		update(MAPPER.BATCH_UPDATE, parameter);
	}

	/**
	 * @describe 批量修改数据，使用给定的参数对象执行特定的INSERT语句
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @throws DataAccessException
	 */
	public void update(String statement, List<Dto> parameter) throws DataAccessException {
		getDao().update(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 根据主键查询数据
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @return
	 */
	public <T> T selectOne(Dto parameter) {
		return selectOne(MAPPER.FIND, parameter);
	}

	/**
	 * @describe 根据主键查询数据，单条数据
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @return
	 */
	public <T> T selectOne(String statement, Dto parameter) {
		return getDao().selectOne(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 根据条件查询
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param parameter
	 * @return
	 */
	public <T> List<T> selectList(Dto parameter) {
		return selectList(MAPPER.FIND_LIST, parameter);
	}

	/**
	 * @describe 根据条件查询，集合
	 * @author caochengde
	 * @data 2018年10月30日
	 *
	 * @param statement
	 * @param parameter
	 * @return
	 */
	public <T> List<T> selectList(String statement, Dto parameter) {
		return getDao().selectList(getNamespace() + statement, parameter);
	}

	/**
	 * @describe 分页查询
	 * @author caochengde
	 * @data 2018年10月31日
	 *
	 * @param parameter
	 * @return
	 */
	public <T> PageInfo<T> findPagingList(Dto parameter) {
		// page：当前页	pageSize：每页的数量
		if(parameter.containsKey("page") && parameter.containsKey("pageSize")) {
			int page = parameter.getInteger("page");
			int pageSize = parameter.getInteger("pageSize");
			// PageHelper帮我们生成分页语句，底层实现原理采用改写sql语句
			PageHelper.startPage(page, pageSize);
		}
		//c 执行查询语句
		List<T> list = selectList(parameter);
		//c 返回给客户端展示
		PageInfo<T> pageInfoList = new PageInfo<T>(list);
		return pageInfoList;
	}
	/**
	 * 用于模糊查询 e yds
	 *
	 * @param parameter
	 * @return
	 */
	public <T> List<T> selectLikeList(Dto parameter) {
		return selectList(MAPPER.LIKE_LIST, parameter);
	}
	/**
	 * yds e 用于模糊分页查询
	 *
	 * @param parameter
	 * @return
	 */
	public <T> PageInfo<T> likePagingList(Dto parameter) {
		// page：当前页 pageSize：每页的数量
		if (parameter.containsKey("page") && parameter.containsKey("pageSize")) {
			int page = parameter.getInteger("page");
			int pageSize = parameter.getInteger("pageSize");
			// PageHelper帮我们生成分页语句，底层实现原理采用改写sql语句
			PageHelper.startPage(page, pageSize);
		}
		// c 执行查询语句
		List<T> list = selectLikeList(parameter);
		// c 返回给客户端展示
		PageInfo<T> pageInfoList = new PageInfo<T>(list);
		return pageInfoList;
	}

}
