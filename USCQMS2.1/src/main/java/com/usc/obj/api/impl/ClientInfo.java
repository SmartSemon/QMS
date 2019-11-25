package com.usc.obj.api.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.usc.obj.api.UserClientIDProvider;

public class ClientInfo implements UserClientIDProvider, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 5142607788610855369L;
	private Map<String, String> infoMap = new HashMap<>();

	public ClientInfo()
	{
	}

	public ClientInfo(Map<String, String> map)
	{
		if (map != null)
		{
			this.putAll(map);
		}
	}

	@Override
	public String getUserClientID()
	{
		return this.infoMap.get("ClientID");
	}

	public void setUserClientID(String clientID)
	{
		this.infoMap.put("ClientID", clientID);
	}

	public Map getInfoMap()
	{
		return infoMap;
	}

	public void setInfoMap(Map<String, String> infoMap)
	{
		this.infoMap = infoMap;
	}

	public void putAll(Map<String, String> map)
	{
		if (map != null)
		{
			this.infoMap.putAll(map);
		}
	}

	public String getClientProperty(String param)
	{
		return this.infoMap.get(param);
	}

	public void setClientProperty(String param1, String param2)
	{
		if (param2 == null)
		{
			this.infoMap.remove(param1);
			return;
		}
		this.infoMap.put(param1, param2);
	}

	public void setUserID(String userID)
	{
		this.infoMap.put("USERID", userID);
	}

	public String getUserID()
	{
		return this.infoMap.get("UserID");
	}

	public void setUserNo(String userNo)
	{
		this.infoMap.put("UserNo", userNo);
	}

	public String getUserNo()
	{
		return this.infoMap.get("UserNo");
	}

	public void setUserName(String userName)
	{
		this.infoMap.put("UserNO", userName);
	}

	public String getUserName()
	{
		return this.infoMap.get("UserName");
	}

	public void setSpersonnelName(String SpersonnelName)
	{
		this.infoMap.put("SpersonnelName", SpersonnelName);
	}

	public String getSpersonnelName()
	{
		return this.infoMap.get("SpersonnelName");
	}

	public void setSpersonnelNO(String SpersonnelNo)
	{
		this.infoMap.put("SpersonnelNO", SpersonnelNo);
	}

	public String getSpersonnelNO()
	{
		return this.infoMap.get("SpersonnelNO");
	}

	public void setDepartment(String dpNo)
	{
		this.infoMap.put("Department", dpNo);
	}

	public void getDepartment()
	{
		this.infoMap.get("Department");
	}
}
