package com.usc.server.md.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.server.DBConnecter;
import com.usc.server.md.ItemGridField;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.ItemPageField;
import com.usc.server.md.ItemRelationPageSign;

/**
 *
 * <p>
 * Title: BeanFactoryConverter
 * </p>
 *
 * <p>
 * Description: JavaBean转换器
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年4月26日
 *
 */
public class ItemRelBeanFactoryConverter
{

	protected static JdbcTemplate jdbcTemplate;
	protected static List<ItemPageField> itemPageFieldList;
	private static List<ItemGridField> itemGridFieldList;
	private static List<ItemRelationPageSign> itemRelationPageSignList;
	private static List<ItemMenu> itemRelationMenuList;

	/**
	 * @param calss
	 * @param resultSet 返回单个实体对象
	 * @return
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
		String className = calss.getSimpleName();
		// 获取字段
		Field[] fields = calss.getDeclaredFields();
		String rootId = resultSet.getString("id");
		String itemId = null;
		try
		{
			itemId = resultSet.getString("itemid");
		} catch (Exception e)
		{
			itemId = null;
		}

		// 遍历fields
		for (Field field : fields)
		{
			// 获取字段名
			String fieldName = field.getName();

			Object fieldVlaue = null;
			try
			{
				resultSet.findColumn(fieldName);
				fieldVlaue = resultSet.getObject(fieldName);
			} catch (SQLException e)
			{
				if ("ItemPage".equals(className)
						&& ("pageFieldList".equals(fieldName) || "pageFieldMap".equals(fieldName)))
				{
					itemPageFieldList = getItemPageFieldList(itemId, rootId);
					switch (fieldName)
					{
					case "pageFieldMap":
						fieldVlaue = ModelInfoToMap.getItemPageField(itemPageFieldList);
						break;

					default:
						fieldVlaue = itemPageFieldList;
						break;
					}

				} else if ("ItemGrid".equals(className)
						&& ("gridFieldList".equals(fieldName) || "gridFieldMap".equals(fieldName)))
				{
					itemGridFieldList = getItemGridFieldList(itemId, rootId);
					switch (fieldName)
					{
					case "gridFieldMap":
						fieldVlaue = ModelInfoToMap.getItemGridField(itemGridFieldList);
						break;

					default:
						fieldVlaue = itemGridFieldList;
						break;
					}
				} else if ("ItemRelationPage".equals(className) && ("itemRelationPageSignMap".equals(fieldName)
						|| "itemRelationPageSignList".equals(fieldName)))
				{
					itemRelationPageSignList = getItemRelationPageSignList(itemId, rootId);
					switch (fieldName)
					{
					case "itemRelationPageSignMap":
						fieldVlaue = ModelInfoToMap.getItemRelationPageSign(itemRelationPageSignList);
						break;

					default:
						fieldVlaue = itemRelationPageSignList;
						break;
					}
				} else if ("ModelRelationShip".equals(className)
						&& ("relationMenuList".equals(fieldName) || "relationMenuMap".equals(fieldName)))
				{
					itemRelationMenuList = getRelationMenuList(itemId, rootId);
					switch (fieldName)
					{
					case "relationMenuMap":
						fieldVlaue = ModelInfoToMap.getItemMenu(itemRelationMenuList);
						break;

					default:
						fieldVlaue = itemRelationMenuList;
						break;
					}
				}
			}

			if (fieldVlaue != null)
			{
				// 获取方法名
				String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// 获取字段类型
				Class<?> type = field.getType();
				Method method = calss.getDeclaredMethod(setMethodName, type);
				method.invoke(object, DataClassType.getValue(type, fieldVlaue));
			}

		}
		return object;
	}

	private static List<ItemMenu> getRelationMenuList(String itemId, String rootId)
	{
		return jdbcTemplate.query(
				"SELECT * FROM usc_model_itemmenu WHERE del=0 AND state='F' AND itemid='" + rootId + "'",
				new MenuRowMapper());
	}

	protected static List<ItemPageField> getItemPageFieldList(String itemId, String rootId)
	{
		return jdbcTemplate.query(getSql(itemId, rootId, "usc_model_property_field"), new PageFieldRowMapper());
	}

	protected static List<ItemGridField> getItemGridFieldList(String itemId, String rootId)
	{

		return jdbcTemplate.query(getSql(itemId, rootId, "usc_model_property"), new GridFieldRowMapper());
	}

	private static List<ItemRelationPageSign> getItemRelationPageSignList(String itemId, String rootId)
	{

		return jdbcTemplate.query("SELECT * FROM usc_model_relationpage_sign WHERE del=0 AND state='F' AND itemid='"
				+ itemId + "' AND rootid='" + rootId + "'", new RelationPageSignRowMapper());
	}

	private static String getSql(String itemId, String rootId, String tableName)
	{
		String sql = "SELECT P.id AS id,P.no AS no,P.name AS name,P.editable AS editable,P.sort AS sort,P.ctime AS ctime,";
		String sql2 = "F.fieldname AS fieldname,F.ftype AS ftype,F.flength AS flength,"
				+ "F.allownull AS allownull,F.accuracy AS accuracy,F.only AS only,"
				+ "F.ispk AS ispk,F.defaultv AS defaultv,F.editor AS editor,F.editparams AS editparams,F.type AS type "
				+ "FROM usc_model_field F,";
		String condition = "WHERE P.del=0 AND P.state='F' AND F.del=0 AND F.state='F' AND P.itemid='" + itemId
				+ "' AND P.rootid='" + rootId + "' AND (F.no=P.no OR P.no is null OR P.no='') AND F.itemid='" + itemId
				+ "'";
		if ("usc_model_property_field".equals(tableName))
		{
			sql += "P.wline AS wline," + sql2 + "usc_model_property_field P " + condition;
		} else
		{
			sql += "P.align AS align,P.width AS width,P.screen AS screen," + sql2 + "usc_model_grid_field P "
					+ condition;
		}
		return sql + " GROUP BY no,id ORDER BY sort,ctime,id";
	}
}
