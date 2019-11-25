package com.usc.server.md.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.server.DBConnecter;
import com.usc.server.md.ItemField;
import com.usc.server.md.ItemGrid;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.ItemPage;
import com.usc.server.md.ItemRelationPage;
import com.usc.util.ObjectHelperUtils;

public class ItemBeanFactoryConverter
{

	private static JdbcTemplate jdbcTemplate;
	private static List<ItemField> itemFieldList = null;
	private static List<ItemMenu> itemMenuList = null;
	private static List<ItemPage> itemPageList = null;
	private static List<ItemGrid> itemGridList = null;
	private static List<ItemRelationPage> itemRelationPageList = null;

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
		String itemId = resultSet.getString("id");

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
				if ("itemFieldMap".equals(fieldName) || "itemFieldList".equals(fieldName))
				{
					itemFieldList = getFieldTypes(itemId);
					if (ObjectHelperUtils.isEmpty(itemFieldList))
					{
						continue;
					} else
					{
						switch (fieldName)
						{
						case "itemFieldMap":
							fieldVlaue = ModelInfoToMap.getItemFieldType(itemFieldList);
							break;

						default:
							fieldVlaue = itemFieldList;
							break;
						}
					}
				}
				if ("itemMenuMap".equals(fieldName) || "itemMenuList".equals(fieldName))
				{
					itemMenuList = getMenuTypes(itemId);
					if (ObjectHelperUtils.isEmpty(itemMenuList))
					{
						continue;
					} else
					{
						switch (fieldName)
						{
						case "itemMenuMap":
							fieldVlaue = ModelInfoToMap.getItemMenu(itemMenuList);
							break;

						default:
							fieldVlaue = itemMenuList;
							break;
						}
					}
				}

				if ("itemPageMap".equals(fieldName) || "itemPageList".equals(fieldName))
				{
					itemPageList = getPageTypes(itemId);
					if (ObjectHelperUtils.isEmpty(itemPageList))
					{
						continue;
					} else
					{
						switch (fieldName)
						{
						case "itemPageMap":
							fieldVlaue = ModelInfoToMap.getItemPage(itemPageList);
							break;

						default:
							fieldVlaue = itemPageList;
							break;
						}

					}
				}
				if ("itemGridMap".equals(fieldName) || "itemGridList".equals(fieldName))
				{
					itemGridList = getGridTypes(itemId);
					if (ObjectHelperUtils.isEmpty(itemGridList))
					{
						continue;
					} else
					{
						switch (fieldName)
						{
						case "itemGridMap":
							fieldVlaue = ModelInfoToMap.getItemGrid(itemGridList);
							break;

						default:
							fieldVlaue = itemGridList;
							break;
						}

					}
				}
				if ("relationPageMap".equals(fieldName) || "relationPageList".equals(fieldName))
				{
					itemRelationPageList = getRelationPage(itemId);
					if (ObjectHelperUtils.isEmpty(itemRelationPageList))
					{
						continue;
					} else
					{
						switch (fieldName)
						{
						case "relationPageMap":
							fieldVlaue = ModelInfoToMap.getItemRelationPage(itemRelationPageList);
							break;

						default:
							fieldVlaue = itemRelationPageList;
							break;
						}

					}
				}

			}

			if (fieldVlaue != null)
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

	private static List<ItemRelationPage> getRelationPage(String itemId)
	{

		return jdbcTemplate.query(getSql(itemId, "usc_model_relationpage"), new RelationPageRowMapper());
	}

	private static String getSql(String itemId, String tableName)
	{
		if (itemId == null || tableName == null)
		{
			return null;
		}
		String sql = "SELECT * FROM " + tableName + " WHERE del=0 AND state='F' AND itemid='" + itemId + "'";
		return sql;

	}

	public static List<ItemField> getFieldTypes(String itemId)
	{
		return jdbcTemplate.query(getSql(itemId, "usc_model_field"), new FieldRowMapper());
	}

	public static List<ItemMenu> getMenuTypes(String itemId)
	{
		return jdbcTemplate.query(getSql(itemId, "usc_model_itemmenu"), new MenuRowMapper());
	}

	public static List<ItemPage> getPageTypes(String itemId)
	{
		return jdbcTemplate.query(getSql(itemId, "usc_model_property"), new PageRowMapper());
	}

	public static List<ItemGrid> getGridTypes(String itemId)
	{
		return jdbcTemplate.query(getSql(itemId, "usc_model_grid"), new GridRowMapper());
	}

}
