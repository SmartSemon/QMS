package com.usc.server.jdbc.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.util.Assert;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.hive.parser.HiveStatementParser;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import com.usc.server.util.StringHelperUtil;
import com.usc.server.util.SystemTime;
import com.usc.util.ObjectHelperUtils;

/**
 * ClassName: DataBaseUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: DataBase SQL Action REASON(可选). <br/>
 * date: 2019年7月31日 下午4:31:39 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class DataBaseUtils
{

	protected static final int defaultPageSize = 200;

	private static int getDefaultPageSize()
	{
		return defaultPageSize;
	}

	public static String getLimit(int pageString)
	{
		int page = (pageString <= 1) ? 1 : pageString;
		return " LIMIT " + getStartLimitRow(page) + "," + getDefaultLimitPageSize();
	}

	public static String getLimit(String pageString)
	{
		int page = (pageString == null || pageString.equals("") || Integer.valueOf(pageString) <= 1) ? 1
				: Integer.valueOf(pageString);
		return " LIMIT " + getStartLimitRow(page) + "," + getDefaultLimitPageSize();
	}

	public static String getLimit(String pageString, String pageSize)
	{
		int page = (pageString == null || pageString.equals("") || Integer.valueOf(pageString) <= 1) ? 1
				: Integer.valueOf(pageString);
		int pageSzie = (pageSize == null || pageSize.equals("")
				|| Integer.valueOf(pageSize) <= getDefaultLimitPageSize()) ? getDefaultLimitPageSize()
						: Integer.valueOf(pageSize);

		return " LIMIT " + getStartLimitRow(page) + "," + pageSzie;
	}

	private static int getStartLimitRow(int page)
	{
		return getDefaultLimitPageSize() * (page - 1);

	}

	public static int getDefaultLimitPageSize()
	{
		return getDefaultPageSize();

	}

	public static String getLastCompareCode(String paramString, int paramInt)
	{
		if (paramString == null)
		{
			return "=";
		}
		paramString = paramString.toLowerCase();
		String str1 = null;
		if (paramString.length() <= paramInt)
		{
			str1 = paramString;
		} else
		{
			str1 = paramString.substring(0, paramInt);
		}
		List<String> list = new ArrayList<String>();
		list.add("<");
		list.add("<=");
		list.add("=");
		list.add("<>");
		list.add(">=");
		list.add(">");
		list.add("like");
		list.add("not like");
		list.add("is");
		list.add("is");
		list.add("in");
		list.add("not in");
		int i = -1;
		Object localObject = "=";
		for (int j = 0; j < list.size(); j++)
		{
			String str2 = (String) list.get(j);
			int k = str1.lastIndexOf(str2);
			if (k >= 0)
			{

				if (k > i)
				{
					i = k;
					localObject = str2;
				}
			}
		}
		return localObject.toString();
	}

	public static String convertSQLQueryTransferCode(String ps1, String ps2)
	{
		ps1 = StringHelperUtil.replaceAll(ps1, "'", "''");
		if (("like".equalsIgnoreCase(ps2)) || ("not like".equalsIgnoreCase(ps2)))
		{
			ps1 = StringHelperUtil.replaceAll(ps1, "[", "[[]");
		}

		return ps1;
	}

	private static Log LOG = LogFactory.getLog(DataBaseUtils.class);

	/**
	 * chinese:检测输入的查询SQL中数据库与表组合形式是否正确 english:check the database and tables in
	 * search sql
	 *
	 * @param sql
	 * @return
	 */
	public static String dbTableFormat(String sql)
	{
		String message = "";
		SQLStatementParser parser = new HiveStatementParser(sql);
		SQLStatement statement = parser.parseStatement();
		HiveSchemaStatVisitor visitor = new HiveSchemaStatVisitor();
		statement.accept(visitor);
		Map<TableStat.Name, TableStat> tableOpt = visitor.getTables();
		LOG.info(tableOpt);
		if (tableOpt.isEmpty())
			return "抱歉，查询的SQL语句中数据库名与表名以下格式 ：\n Database.Table 存在\n ，请修改后再进行查询！";
		for (TableStat.Name key : tableOpt.keySet())
		{
			if (!key.toString().contains("."))
			{
				message = "抱歉，查询的SQL语句中数据库名与表名以下格式 ：\n Database.Table 存在\n ，请修改后再进行查询！";
				return message;
			}
		}
		return message;
	}

	public static String map2InsertSql(Map<String, Object> insertData, String... tableNames)
	{
		Assert.notEmpty(insertData);
		Object t = null;
		if (ObjectHelperUtils.isEmpty(tableNames))
		{
			t = insertData.get("tableName");
			Assert.notNull(t);
			tableNames = new String[]
			{ String.valueOf(t) };
			insertData.remove(t);
		}
		StringBuffer sql = new StringBuffer();
		for (String table : tableNames)
		{
			sql.append("INSERT INTO " + table);
			StringBuffer fields = new StringBuffer(" (");
			StringBuffer values = new StringBuffer(" VALUES (");
			int i = 1;
			int ks = insertData.keySet().size();
			for (String f : insertData.keySet())
			{
				Object val = insertData.get(f);
				if (val != null)
				{
					if (val instanceof Date)
					{
						DateFormatter af = SystemTime.formatter;
						val = "'" + af.print((Date) val, Locale.getDefault()) + "'";

					}
					if (val instanceof String)
					{
						values.append(val);
					}
					if (val instanceof Integer)
					{
						values.append(val);
					}
					if (val instanceof Boolean)
					{
						values.append(BooleanUtils.toInteger((boolean) val));
					}
				} else
				{
					values.append("null");
				}

				if (i != ks)
				{
					fields.append(f + ",");
					values.append(",");

				} else
				{
					fields.append(f + ")");
					values.append(")");
				}
				i++;
			}
			sql.append(fields.toString() + values + ";");
		}

		return null;

	}

	public static String mapArray2InsertSql(String tableName, String[] fields, Map<String, Object>... insertDatas)
	{
		Assert.notNull(tableName);
		Assert.notEmpty(insertDatas);
		if (ObjectHelperUtils.isEmpty(fields))
		{
			fields = insertDatas[0].keySet().toArray(fields);
			Assert.notEmpty(fields);
		}
		StringBuffer sql = new StringBuffer("INSERT INTO " + tableName);
		StringBuffer fs = new StringBuffer("(");
		for (String string : fields)
		{
			fs.append(string + " ");
		}
		sql.append(fs.toString().trim() + ")");
		int ks = fields.length;
		int dlength = insertDatas.length;
		int j = 1;
		for (Map<String, Object> data : insertDatas)
		{
			if (data.isEmpty())
			{
				throw new NullPointerException("insertDatas[" + (j - 1) + "] isEmpty");
			}
			if (data.containsKey("id") && data.containsKey("ID"))
			{
				throw new NullPointerException("insertDatas[" + (j - 1) + "] Id does not exist");
			}
			StringBuffer values = new StringBuffer("(");

			for (int i = 0; i < ks; i++)
			{
				String f = fields[i];
				Object val = data.get(f);
				if (val != null)
				{
					if (val instanceof Date)
					{
						DateFormatter af = SystemTime.formatter;
						val = "'" + af.print((Date) val, Locale.getDefault()) + "'";

					}
					if (val instanceof String)
					{
						values.append(val);
					}
					if (val instanceof Integer)
					{
						values.append(val);
					}
					if (val instanceof Boolean)
					{
						values.append(BooleanUtils.toInteger((boolean) val));
					}
				} else
				{
					values.append("null");
				}

				if (i != ks)
				{
					values.append(",");

				} else
				{
					values.append(")");
					if (j != dlength)
					{
						values.append(",");
					}
//					else {
//						values.append(";");
//					}
				}
			}
			j++;
		}

		return sql.toString();

	}

	public static String mapList2InsertSql(String tableName, String[] fields, List<Map<String, Object>> insertDatas)
	{
		Assert.notNull(tableName);
		Assert.notEmpty(insertDatas);
		if (ObjectHelperUtils.isEmpty(fields))
		{
			fields = insertDatas.get(0).keySet().toArray(fields);
			Assert.notEmpty(fields);
		}
		StringBuffer sql = new StringBuffer("INSERT INTO " + tableName);
		StringBuffer fs = new StringBuffer("(");
		for (String string : fields)
		{
			fs.append(string + " ");
		}
		sql.append(fs.toString().trim() + ")");
		int ks = fields.length;
		int dlength = insertDatas.size();
		int j = 1;
		for (Map<String, Object> data : insertDatas)
		{
			if (data.isEmpty())
			{
				throw new NullPointerException("insertDatas[" + (j - 1) + "] isEmpty");
			}
			if (data.containsKey("id") && data.containsKey("ID"))
			{
				throw new NullPointerException("insertDatas[" + (j - 1) + "] Id does not exist");
			}
			StringBuffer values = new StringBuffer("(");

			for (int i = 0; i < ks; i++)
			{
				String f = fields[i];
				Object val = data.get(f);
				if (val != null)
				{
					if (val instanceof Date)
					{
						DateFormatter af = SystemTime.formatter;
						val = "'" + af.print((Date) val, Locale.getDefault()) + "'";

					}
					if (val instanceof String)
					{
						values.append("'" + val + "'");
					}
					if (val instanceof Integer)
					{
						values.append(val);
					}
					if (val instanceof Boolean)
					{
						values.append(BooleanUtils.toInteger((boolean) val));
					}
				} else
				{
					values.append("null");
				}

				if (i != ks)
				{
					values.append(",");

				} else
				{
					values.append(")");
					if (j != dlength)
					{
						values.append(",");
					}
//					else {
//						values.append(";");
//					}
				}
			}
			j++;
		}

		return sql.toString();

	}
}
