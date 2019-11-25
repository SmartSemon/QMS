package com.usc.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.sys.online.OnlineUsers;
import com.usc.app.util.UserInfoUtils;
import com.usc.app.util.servlet.ServerletUtils;
import com.usc.app.util.tran.FormatPromptInformation;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.autho.MD5.MD5Util;

@Configuration
@WebFilter(filterName = "implFilter", urlPatterns = "/login")
@Order(100)
public class LogInOrOutSystemFilter implements Filter
{
	FilterConfig filterConfig = null;
	private static final Set<String> ALLOWED_PATHS = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList("/login", "/logout")));

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
		if (ALLOWED_PATHS.contains(path))
		{
			ServletRequest requestWapper = ServerletUtils.getServletRequestWapper(request);
			if (requestWapper != null)
			{
				String json = ServerletUtils.getRequestJsonParam((HttpServletRequest) requestWapper);
				if (path.equals("/login"))
				{
					JSONObject jsonObject = JSONObject.parseObject(json);
					String userName = (String) jsonObject.get("userName");
					String password = String.valueOf(jsonObject.get("userPassword"));
					boolean flag = UserInfoUtils.userExistence(userName);
					if (!flag)
					{
						try
						{
							ServerletUtils.returnResponseJson(response,
									new JSONObject(StandardResultTranslate
											.getResult(FormatPromptInformation.getLoginMsg("User_Exist_0"), false))
													.toJSONString());
						} catch (Exception e)
						{
							e.printStackTrace();
						}
					} else
					{
						try
						{
							if (!MD5Util.validMD5Legitimacy(password, UserInfoUtils.getPassWord(userName)))
							{
								ServerletUtils.returnResponseJson(response,
										new JSONObject(StandardResultTranslate.getResult(
												FormatPromptInformation.getLoginMsg("Incorrect_Password"), false))
														.toJSONString());
								return;
							}
							if (jsonObject.containsKey("force"))
							{
								boolean force = (boolean) jsonObject.get("force");
								if (!force)
								{
									ServerletUtils.returnResponseJson(response,
											new JSONObject(StandardResultTranslate.getResult(
													FormatPromptInformation.getLoginMsg("Login_Fail"), false))
															.toJSONString());
									return;
								}

							} else
							{
								if (OnlineUsers.isOnline(userName))
								{
									Map map = StandardResultTranslate
											.getResult(FormatPromptInformation.getLoginMsg("Login_Force"), true);
									map.put("force", true);
									ServerletUtils.returnResponseJson(response, new JSONObject(map).toJSONString());
									return;
								}
							}
							chain.doFilter(requestWapper, response);
							return;
						} catch (Exception e)
						{
							try
							{
								ServerletUtils.returnResponseJson(response,
										new JSONObject(StandardResultTranslate.getResult(
												FormatPromptInformation.getLoginMsg("Incorrect_Password"), false))
														.toJSONString());
							} catch (Exception e1)
							{
								e1.printStackTrace();
								return;
							}
						}

					}

				} else
				{
					chain.doFilter(requestWapper, response);
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		this.filterConfig = null;
	}

}
