package com.usc.server.md.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.server.DBConnecter;
import com.usc.server.md.ModelClassViewTreeNode;
import com.usc.util.ObjectHelperUtils;

public class ViewBeanFactoryConverter
{
	private static List<ModelClassViewTreeNode> viewTreeNodeList = null;
	private static JdbcTemplate jdbcTemplate;

	/**
	 * @return
	 * @see 返回单个实体对象
	 */
	public static <T> T getBean(Class<T> calss, ResultSet resultSet)
	{
		try
		{
			jdbcTemplate = new JdbcTemplate(DBConnecter.getDataSource());
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		T object = null;
		try
		{
			object = createBean(calss, resultSet);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return object;
	}

	/**
	 * @see 返回实体对象集
	 */
	public static <T> List<T> getBeans(Class<T> calss, ResultSet resultSet)
	{
		List<T> ts = null;
		try
		{
			ts = new ArrayList<>();
			while (resultSet.next())
			{
				ts.add(createBean(calss, resultSet));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return ts;
	}

	/**
	 * @说明 创建实体
	 * @param calss
	 * @param resultSet
	 * @return 实体对象
	 * @throws Exception
	 */
	private static <T> T createBean(Class<T> calss, ResultSet resultSet) throws Exception
	{
		T object = calss.newInstance();
		// 获取字段
		Field[] fields = calss.getDeclaredFields();
		String viewId = resultSet.getString("id");

		// 遍历fields
		for (Field field : fields)
		{

			// 获取字段名
			String fieldName = field.getName();

			Object fieldVlaue = null;
			try
			{
				resultSet.findColumn(fieldName.toLowerCase());
				fieldVlaue = resultSet.getObject(fieldName);
			} catch (SQLException e)
			{

				if (fieldName.equalsIgnoreCase("serialVersionUID"))
				{
					continue;
				}

				if ("classViewNodeList".equals(fieldName) || "classViewNodeMap".equals(fieldName))
				{
					viewTreeNodeList = getTreeNodes(viewId);
					if (ObjectHelperUtils.isEmpty(viewTreeNodeList))
					{
						continue;
					} else
					{
						if ("classViewNodeMap".equals(fieldName))
						{
							fieldVlaue = ModelInfoToMap.getClassTreeNode(viewTreeNodeList);

						} else
						{
							fieldVlaue = viewTreeNodeList;
						}

					}
				} else if ("rootNode".equals(fieldName))
				{
					for (int i = 0; i < viewTreeNodeList.size(); i++)
					{
						ModelClassViewTreeNode node = viewTreeNodeList.get(i);
						if ("0".equals(node.getPid()))
						{
							fieldVlaue = node;
							break;
						}
					}
				}

			}

			if (!ObjectHelperUtils.isEmpty(fieldVlaue))
			{
				// 获取方法名
				String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// 获取field类型
				Class<?> type = field.getType();
				Method method = calss.getDeclaredMethod(setMethodName, type);
				method.invoke(object, fieldVlaue);
			}
		}
		return object;
	}

	private static String getSql(String viewId, String tableName)
	{
		if (viewId == null || tableName == null)
		{
			return null;
		}
		String sql = "SELECT * FROM " + tableName + " WHERE del=0 AND state='F' AND itemid='" + viewId + "'";
		return sql;

	}

	public static List<ModelClassViewTreeNode> getTreeNodes(String viewId)
	{
		return jdbcTemplate.query(getSql(viewId, "usc_model_classview_node"), new ModelClassViewTreeNodeMapper());
	}

}
