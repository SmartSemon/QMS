package com.usc.server.md.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.server.md.ItemMenu;

public class RelationPageBeanFactoryConverter
{

	private static JdbcTemplate jdbcTemplate;

	/**
	 * @return
	 * @see 返回单个实体对象
	 */
	public static <T> T getBean(JdbcTemplate jdbcTemplate1, Class<T> calss, ResultSet resultSet)
	{
		jdbcTemplate = jdbcTemplate1;
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
		String itemId = resultSet.getString("id");
		String relationid = resultSet.getString("relationid");
		// 遍历fields
		for (Field field : fields)
		{

			// 获取字段名
			String fieldName = field.getName();
			// 获取方法名
			String setMethodName = "set" + (char) (fieldName.charAt(0) - 32) + fieldName.substring(1);
			// 获取field类型
			Class<?> type = field.getType();
			Object fieldVlaue = null;
			try
			{
				resultSet.findColumn(fieldName);
				fieldVlaue = resultSet.getObject(fieldName);
			} catch (SQLException e)
			{

				if (fieldName.equalsIgnoreCase("serialVersionUID"))
				{
					continue;
				}
				if (fieldName.equals("RelationItem"))
				{
					Map<String, Object> RelationItem = jdbcTemplate.queryForMap((getSql(relationid, "usc_model_item")));
					if (RelationItem == null || RelationItem.size() < 1)
					{
						continue;
					} else
					{
						fieldVlaue = RelationItem;

					}
				}
				if (fieldName.equals("RelMenuTypesMap"))
				{
					List<ItemMenu> menuTypes = jdbcTemplate.query(getSql(itemId, "usc_model_muse"),
							new MenuRowMapper());
					if (menuTypes == null || menuTypes.size() < 1)
					{
						continue;
					} else
					{
						fieldVlaue = ModelInfoToMap.getItemMenu(menuTypes);

					}
				}
				if (fieldName.equals("RelMenuList"))
				{
					List<ItemMenu> menuTypes = jdbcTemplate.query(getSql(itemId, "usc_model_muse"),
							new MenuRowMapper());
					if (menuTypes == null || menuTypes.size() < 1)
					{
						continue;
					} else
					{
						fieldVlaue = menuTypes;

					}
				}

				continue;
			}

			Method method = calss.getDeclaredMethod(setMethodName, type);
			if (fieldVlaue != null)
			{
				method.invoke(object, fieldVlaue);
			}
		}
		return object;
	}

	private static String getSql(String itemId, String tableName)
	{
		if (itemId == null || tableName == null)
		{
			return null;
		}
		if (tableName.equals("usc_model_item"))
		{
			return "SELECT itemno AS relItem,tablename AS relTableName FROM usc_model_item WHERE del=0 AND state='F' "
					+ "AND itemno=(SELECT relitem FROM usc_model_correlation WHERE del=0 AND state='F' " + "AND id='"
					+ itemId + "')";
		}
		String sql = "SELECT * FROM " + tableName + " WHERE del=0 AND state='F' "
				+ "AND EXISTS(SELECT 1 FROM usc_model_relation WHERE del=0 AND state='F' " + "AND itemid='" + itemId
				+ "' AND infoid=" + tableName + ".id)";
		return sql;

	}

}
