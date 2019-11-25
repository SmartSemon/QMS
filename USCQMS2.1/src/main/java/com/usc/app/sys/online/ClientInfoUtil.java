package com.usc.app.sys.online;

import javax.servlet.http.HttpServletRequest;

public class ClientInfoUtil
{
	public static String getIPAddress(HttpServletRequest request)
	{
		String ip = null;

		// X-Forwarded-For：Squid
		String ipAddresses = request.getHeader("X-Forwarded-For");

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
		{
			// Proxy-Client-IP：apache
			ipAddresses = request.getHeader("Proxy-Client-IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
		{
			// WL-Proxy-Client-IP：weblogic
			ipAddresses = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
		{
			// HTTP_CLIENT_IP
			ipAddresses = request.getHeader("HTTP_CLIENT_IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
		{
			// X-Real-IP：nginx
			ipAddresses = request.getHeader("X-Real-IP");
		}

		// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		if (ipAddresses != null && ipAddresses.length() != 0)
		{
			ip = ipAddresses.split(",")[0];
		}

		// 还是不能获取到，最后再通过request.getRemoteAddr();获取
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
		{
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getOsAndBrowserInfo(HttpServletRequest request)
	{
		String browserDetails = request.getHeader("User-Agent");
		String userAgent = browserDetails;
		String user = userAgent.toLowerCase();

		String os = "";
		String browser = "";

		// =================OS Info=======================
		if (userAgent.toLowerCase().indexOf("windows") >= 0)
		{
			os = "Windows";
		} else if (userAgent.toLowerCase().indexOf("mac") >= 0)
		{
			os = "Mac";
		} else if (userAgent.toLowerCase().indexOf("x11") >= 0)
		{
			os = "Unix";
		} else if (userAgent.toLowerCase().indexOf("android") >= 0)
		{
			os = "Android";
		} else if (userAgent.toLowerCase().indexOf("iphone") >= 0)
		{
			os = "IPhone";
		} else
		{
			os = "UnKnown, More-Info: " + userAgent;
		}

		// ===============Browser===========================
		if (user.contains("edge"))
		{
			browser = (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("msie"))
		{
			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		} else if (user.contains("safari") && user.contains("version"))
		{
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
					+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (user.contains("opr") || user.contains("opera"))
		{
			if (user.contains("opera"))
			{
				browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
						+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			} else if (user.contains("opr"))
			{
				browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
						.replace("OPR", "Opera");
			}

		} else if (user.contains("chrome"))
		{
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)
				|| (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1)
				|| (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1))
		{
			browser = "Netscape-?";

		} else if (user.contains("firefox"))
		{
			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("rv"))
		{
			String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
			browser = "IE" + IEVersion.substring(0, IEVersion.length() - 1);
		} else
		{
			browser = "UnKnown, More-Info: " + userAgent;
		}

		return "<" + os + "> " + browser;

	}
}
