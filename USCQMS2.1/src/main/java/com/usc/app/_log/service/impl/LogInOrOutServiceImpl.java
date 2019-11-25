package com.usc.app._log.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.usc.app._log.service.LogInOrOutService;
import com.usc.app.sys.online.OnlineUsers;
import com.usc.app.util.JwtUtils;
import com.usc.app.util.UserInfoUtils;
import com.usc.app.util.tran.FormatPromptInformation;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.obj.api.bean.UserInformation;
import com.usc.obj.api.impl.USCServerBeanProvider;
import com.usc.server.syslog.LOGActionEnum;

@Service("logInOrOutService")
public class LogInOrOutServiceImpl implements LogInOrOutService
{

	@Override
	public Map Login(Map parameter)
	{
		String userName = (String) parameter.get("userName");
		HttpServletRequest request = (HttpServletRequest) parameter.get("request");
		HttpServletResponse response = (HttpServletResponse) parameter.get("response");
		return clientInfo(userName, request, response);

	}

	private Map clientInfo(String userName, HttpServletRequest request, HttpServletResponse response)
	{

		String ClientID = null;
		String token = null;
		List<Map<String, Object>> memus = Navigation.getAuthorized(userName);
		Map<String, Object> result = StandardResultTranslate
				.getResult(FormatPromptInformation.getLoginMsg("Login_Success"), memus);
		UserInformation userInformation = UserInfoUtils.getUserInformation(userName);
		if (UserInfoUtils.isSuperAdministrator(userName))
		{
			ClientID = userName;
			userInformation.setClientID(ClientID);
			Map map = new HashMap();
			map.put("UserName", userName);
			token = JwtUtils.buildJwt(map);
		} else
		{
			if (userInformation == null)
			{
				result.put("flag", false);
				result.put("info", FormatPromptInformation.getLoginMsg("UserInfo_Exist_0"));
				return result;
			}
			ClientID = UserInfoUtils.getUserWKContextID(userInformation);
			userInformation.setClientID(ClientID);
			token = JwtUtils.buildJwt(UserInfoUtils.createUserInfoMap(userName));

		}
		if (userInformation != null)
		{
			result.put("employeeName", userInformation.getEmployeeName());
			result.put("userName", userInformation.getUserName());
			result.put("userId", userInformation.getUserID());
			UserInfoUtils.putUserInfomation(userInformation);
		}
		if (ClientID != null)
		{
			result.put("clientID", ClientID);
		}
		result.put("token", "Bearer " + token);
		OnlineUsers.addOnlineUser(request, userInformation);
		USCServerBeanProvider.getSystemLogger().writeLOGINLog(userInformation, LOGActionEnum.LOGIN);
		return result;
	}

	@Override
	public Map Logout(Map parameter)
	{
//		HttpServletRequest request = (HttpServletRequest) parameter.get("request");
//		HttpServletResponse response = (HttpServletResponse) parameter.get("response");
		String userName = (String) parameter.get("userName");
		if (OnlineUsers.isOnline(userName))
		{
			USCServerBeanProvider.getSystemLogger().writeLOGOUTLog(UserInfoUtils.getUserInformation(userName),
					LOGActionEnum.LOGOUT);
			OnlineUsers.removeUser(userName);
		}

		return StandardResultTranslate.getResult(FormatPromptInformation.getLoginMsg("Logout_Success"), true);
	}

}
