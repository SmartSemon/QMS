package com.usc.app.bs.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usc.app.bs.dao.BaseDao;
import com.usc.dto.Dto;


/**
 * @describe 数据库访问对象接口实现类
 * @author caochengde
 * @data 2018年11月20日
 *
 */
@Repository("baseDao")
public class BaseDaoImpl extends SqlSessionDaoSupport implements BaseDao{


	//c 注入SqlSessionFactory，未注入时运行会报错
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	//c 插入数据
	@Override
	public int insert(String statement, Object parameter) throws DataAccessException {
		return getSqlSession().insert(statement, parameter);
	}

	//c 删除数据
	@Override
	public int delete(String statement, Object parameter) throws DataAccessException {
		return getSqlSession().delete(statement, parameter);
	}

	//c 修改数据
	@Override
	public int update(String statement, Object parameter) throws DataAccessException {
		return getSqlSession().update(statement, parameter);
	}

	//c 查询单条数据
	@Override
	public <T> T selectOne(String statement, Object parameter) throws DataAccessException {
		return getSqlSession().selectOne(statement, parameter);
	}

	//c 查询数据集合
	@Override
	public <T> List<T> selectList(String statement, Object parameter) throws DataAccessException {
		return getSqlSession().selectList(statement, parameter);
	}


	List<Dto> dtoList = new ArrayList<Dto>();

	@Override
	public <T> List<T> queryListDto(String table,String condition, String orderFields,
			String limit) throws DataAccessException {
		if (table==null) {
			return null;
		}

		return (List<T>) dtoList;
	}

}



