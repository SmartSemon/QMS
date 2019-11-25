package com.usc.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.sys.online.ClientInfoUtil;
import com.usc.app.sys.online.OnlineUsers;
import com.usc.app.sys.online.OnlineUsers.OnlineUserInfo;
import com.usc.app.util.UserInfoUtils;
import com.usc.app.util.servlet.ServerletUtils;
import com.usc.app.util.tran.FormatPromptInformation;
import com.usc.obj.api.bean.UserInformation;
import com.usc.util.ObjectHelperUtils;

//@Configuration
//@WebFilter(filterName = "CharsetFilter", urlPatterns = "/dcm/access", initParams =
//{ @WebInitParam(name = "charset", value = "utf-8") })
//@Order(2)
public class ImplClassFilter implements Filter
{

	FilterConfig filterConfig = null;
	private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("/dcm",
			"/sysModelToWbeClient", "/ModelItemRelationInfo", "/modelSynchronousDBService", "/sysModelItem")));

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		String path = ((HttpServletRequest) request).getRequestURI()
				.substring(((HttpServletRequest) request).getContextPath().length()).replaceAll("[/]+$", "");
		if (ALLOWED_PATHS.contains(path.substring(0, path.lastIndexOf("/"))))
		{
			JSONObject jsonObject = new JSONObject();
			HttpServletRequest srequest = (HttpServletRequest) request;
			HttpServletResponse sresponse = (HttpServletResponse) response;
			if (!doF(srequest, sresponse, jsonObject))
			{
				try
				{
					ServerletUtils.returnResponseJson(response, jsonObject.toJSONString());
					return;
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			} else
			{
				chain.doFilter(request, response);
			}
		} else
		{
			chain.doFilter(request, response);
		}

	}

	private boolean doF(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject)
	{

		String ClientID = request.getHeader("clientID");
		UserInformation userInformation = null;
		if (ObjectHelperUtils.isEmpty(ClientID))
		{
			jsonObject.put("flag", false);
			jsonObject.put("info", FormatPromptInformation.getSystemCodeMsg("UnauthorizedOperation_INFO") + " ["
					+ FormatPromptInformation.getSystemCodeMsg("UnauthorizedOperation_CODE") + "]");
			return false;
		} else
		{
			OnlineUserInfo user = OnlineUsers.getOnClientUser(ClientID);
			if (user == null)
			{
				getErrorJson(jsonObject);
				return false;
			}
			String userName = user.getUserName();
			userInformation = UserInfoUtils.getUserInformation(userName);
			userInformation.setClientID(ClientID);
			if (!OnlineUsers.isOnline(userName))
			{
				getErrorJson(jsonObject);
			} else
			{
				String oip = OnlineUsers.getOnUser(userName).getIp();
				String nip = ClientInfoUtil.getIPAddress(request);
				String osBowser = OnlineUsers.getOnUser(userName).getOsBowser();
				String nosBowser = ClientInfoUtil.getOsAndBrowserInfo(request);
				if (!oip.equals(nip) || !osBowser.equals(nosBowser))
				{
					getErrorJson(jsonObject);

				}
			}
		}

		if (userInformation != null)
		{
			OnlineUsers.setOnlieUserLastTime(userInformation.getUserName());
		}

		return true;

	}

	public void getErrorJson(JSONObject jsonObject)
	{
		jsonObject.put("flag", false);
		jsonObject.put("path", FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_PATH"));
		jsonObject.put("info", FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_INFO") + " ["
				+ FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_CODE") + "]");
	}

	@Override
	public void destroy()
	{
		this.filterConfig = null;
	}
}
