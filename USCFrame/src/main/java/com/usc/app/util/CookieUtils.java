package com.usc.app.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils
{

	public static void setCookie(HttpServletResponse response, String key, String value)
	{
		Cookie cookie;
		try
		{
			cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
			cookie.setPath("/");
			// cookie.setMaxAge(3600);//设置时间 单位秒s
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	public static String getCookie(HttpServletRequest request, String key)
	{

		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				if (cookie.getName().equals(key))
				{
					try
					{
						return URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

}
