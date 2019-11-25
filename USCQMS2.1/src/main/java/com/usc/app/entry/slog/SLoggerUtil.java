package com.usc.app.entry.slog;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.query.rt.QueryReturnRequest;
import com.usc.server.jdbc.DBUtil;

public class SLoggerUtil implements QueryReturnRequest
{
	private JSONObject jsonObject;

	public SLoggerUtil(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response)
	{
		this.jsonObject = jsonObject;
	}

	public Map getLogDetails()
	{
		String conString = "";
		String action = this.jsonObject.getString("action");
		switch (action)
		{
		case "S":
			conString = "type='S'";
			break;
		case "LOGIN":
			conString = "type='S' AND action='LOGIN'";
			break;
		case "LOGOUT":
			conString = "type='S' AND action='LOGOUT'";
			break;
		case "D":
			conString = "type='D'";
			break;
		case "NEW":
			conString = "type='D' AND action='NEW'";
			break;
		case "DELETE":
			conString = "type='D' AND action='DELETE'";
			break;
		case "MODIFY":
			conString = "type='D' AND action='MODIFY'";
			break;
		case "F":
			conString = "type='F'";
			break;
		case "UPLOAD":
			conString = "type='F' AND action='UPLOAD'";
			break;
		case "DOWNLOAD":
			conString = "type='F' AND action='DOWNLOAD'";
			break;

		default:
			conString = "type='D' AND action='OTHER'";
			break;
		}
		List dataList = DBUtil.getSQLResultByCondition("SYSLOG", conString);
		return queryTrue(dataList);
	}
}
