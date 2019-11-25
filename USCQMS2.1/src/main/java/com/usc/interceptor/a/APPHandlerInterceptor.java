package com.usc.interceptor.a;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.usc.cache.redis.RedisUtil;

public abstract class APPHandlerInterceptor implements HandlerInterceptor
{
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	protected RedisUtil redis = new RedisUtil();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{

		return beforeHandle(request, response, handler);
	}

	public abstract boolean beforeHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception
	{
		afterHandle(request, response, handler, modelAndView);
	}

	public abstract void afterHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{
		afterReaderCompletion(request, response, handler, ex);
	}

	protected abstract void afterReaderCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception;
}
