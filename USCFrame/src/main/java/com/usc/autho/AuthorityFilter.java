package com.usc.autho;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.entry.DefaultEventProcessor;
import com.usc.app.sys.online.ClientInfoUtil;
import com.usc.app.sys.online.OnlineUsers;
import com.usc.app.util.UserInfoUtils;
import com.usc.app.util.tran.FormatPromptInformation;
import com.usc.obj.api.bean.UserInformation;

public class AuthorityFilter
{

	public static boolean doFilter(DefaultEventProcessor processor, HttpServletRequest request,
			HttpServletResponse response, String userName)
	{

		JSONObject jsonObject = new JSONObject();
		UserInformation userInformation = UserInfoUtils.getUserInformation(userName);

		if (!OnlineUsers.isOnline(userName))
		{
			return getErrorJson(processor, jsonObject);
		} else
		{
			String oip = OnlineUsers.getOnUser(userName).getIp();
			String nip = ClientInfoUtil.getIPAddress(request);
			String osBowser = OnlineUsers.getOnUser(userName).getOsBowser();
			String nosBowser = ClientInfoUtil.getOsAndBrowserInfo(request);
			if (!oip.equals(nip) || !osBowser.equals(nosBowser))
			{
				return getErrorJson(processor, jsonObject);
			}
		}
		String ClientID = request.getHeader("clientID");
		if (ClientID == null)
		{
			jsonObject.put("flag", false);
			jsonObject.put("info", FormatPromptInformation.getSystemCodeMsg("UnauthorizedOperation_INFO") + " ["
					+ FormatPromptInformation.getSystemCodeMsg("UnauthorizedOperation_CODE") + "]");
			processor.setResultMap(jsonObject);
			return false;
		}
		userInformation.setClientID(ClientID);
		OnlineUsers.setOnlieUserLastTime(userName);
		return true;

	}

	public static boolean getErrorJson(DefaultEventProcessor processor, JSONObject jsonObject)
	{
		jsonObject.put("flag", false);
		jsonObject.put("path", FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_PATH"));
		jsonObject.put("info", FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_INFO") + " ["
				+ FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_CODE") + "]");
		processor.setResultMap(jsonObject);
		return false;
	}

}
