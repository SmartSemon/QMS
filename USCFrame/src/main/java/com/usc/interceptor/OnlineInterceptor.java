package com.usc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.util.UserInfoUtils;
import com.usc.app.util.servlet.ServerletUtils;
import com.usc.app.util.tran.FormatPromptInformation;
import com.usc.interceptor.a.APPHandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OnlineInterceptor extends APPHandlerInterceptor
{
	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");

	public boolean beforeHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		Object object = request.getSession().getAttribute(UserInfoUtils.USER_SESSION_KEY);
		if (object == null)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("flag", false);
			jsonObject.put("path", FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_PATH"));
			jsonObject.put("info", FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_INFO") + " ["
					+ FormatPromptInformation.getSystemCodeMsg("OfflineLoginAgain_CODE") + "]");
			ServerletUtils.returnResponseJson(response, jsonObject.toJSONString());
			return false;
		} else
		{
			String clientID = request.getHeader("clientID");
			if (clientID == null || clientID.length() == 0)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("flag", false);
				jsonObject.put("info", FormatPromptInformation.getSystemCodeMsg("UnauthorizedOperation_INFO") + " ["
						+ FormatPromptInformation.getSystemCodeMsg("UnauthorizedOperation_CODE") + "]");
				ServerletUtils.returnResponseJson(response, jsonObject.toJSONString());
				return false;
			}
		}
		long beginTime = System.currentTimeMillis();
		startTimeThreadLocal.set(beginTime);
		return true;
	}

	public void afterHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception
	{
		long endTime = System.currentTimeMillis();
		long beginTime = startTimeThreadLocal.get();
		long consumeTime = endTime - beginTime;
		if (consumeTime > 500)
		{
			log.warn(String.format("%s consume %d millis", request.getRequestURI(), consumeTime));
		}
	}

	public void afterReaderCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) throws Exception
	{
//		System.out.println("》》》》》》》》》》》》》》》》》》》页面渲染完成");
	}
}
