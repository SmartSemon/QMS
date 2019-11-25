package com.usc.app.annotation.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.usc.app.annotation.SystemLog;
import com.usc.app.sys.online.ClientInfoUtil;
import com.usc.app.sys.online.OnlineUsers;
import com.usc.server.syslog.LoggingServer;
import com.usc.server.syslog.SLOGObject;
import com.usc.server.util.LoggerFactory;

@Aspect
@Component
public class SystemLogAspect
{
	@Autowired
	private LoggingServer loggingServer;

//	private static final Logger LOGGER = Logger.getLogger(SystemLogAspect.class);
	@Pointcut("@annotation(com.usc.app.annotation.SystemLog)")
	public void serviceAspect()
	{
	}

	@After("serviceAspect()")
	public void doLogging(JoinPoint joinPoint)
	{
		LoggerFactory.logInfo("-----------开始记录日志");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String ip = ClientInfoUtil.getIPAddress(request);
		System.err.println(ip);
		String ClientID = request.getParameter("clientID");
		String userName = OnlineUsers.getOnClientUser(ClientID).getUserName();
		SLOGObject slogObject = new SLOGObject();
		String table = null;
		String details = null;
		String remark = null;
		try
		{
			table = getServiceMthodTableType(joinPoint);
			details = getServiceMthodDescription(joinPoint);
			remark = getServiceMthodParams(joinPoint);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		slogObject.setIP(ip);
		slogObject.setCuser(userName);
		slogObject.setACTION(table);
		slogObject.setDETAILS(details);
		slogObject.setOBJTN(table);
		slogObject.setRemark(remark);
		loggingServer.save(slogObject);
	}

	private String getServiceMthodDescription(JoinPoint joinPoint) throws Exception
	{
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods)
		{
			if (method.getName().equals(methodName))
			{
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length)
				{
					description = method.getAnnotation(SystemLog.class).description();
					break;
				}
			}
		}
		return description;
	}

	/**
	 * getServiceMthodTableType:获取注解中对方法的数据表类型 用于service层注解 . <br/>
	 *
	 * @author lcma
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	private String getServiceMthodTableType(JoinPoint joinPoint) throws Exception
	{
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String tableType = null;
		for (Method method : methods)
		{
			if (method.getName().equals(methodName))
			{
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length)
				{
					tableType = method.getAnnotation(SystemLog.class).objType();
					break;
				}
			}
		}
		return tableType;
	}

	/**
	 * getServiceMthodParams:获取json格式的参数. <br/>
	 *
	 * @author lcma
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	private String getServiceMthodParams(JoinPoint joinPoint) throws Exception
	{
		Object[] arguments = joinPoint.getArgs();
//       String params = JacksonUtil.toJSon(arguments);
		String params = null;
		return params;
	}

}
