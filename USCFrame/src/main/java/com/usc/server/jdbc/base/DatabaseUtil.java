package com.usc.server.jdbc.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.usc.server.DBConnecter;

/**
 * ClassName: DatabaseUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: DataBase Connection Action. <br/>
 * date: 2019年7月31日 下午4:29:52 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class DatabaseUtil
{

	public static ResultSet executeQuery(PreparedStatement localPreparedStatement, String sql) throws SQLException
	{
		return localPreparedStatement.executeQuery(sql);
	}

	public static Connection getServerConnection() throws Exception
	{
		return DBConnecter.getConnection();
	}

	public static void cleanUp(Connection conn)
	{
		if (conn != null)
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

	}

	public static void cleanUp(PreparedStatement localPreparedStatement)
	{
		if (localPreparedStatement != null)
		{
			try
			{
				localPreparedStatement.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

	}

}
