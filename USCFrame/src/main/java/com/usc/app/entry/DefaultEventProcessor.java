package com.usc.app.entry;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.usc.app.action.AppActionFactory;
import com.usc.app.action.i.AppAction;
import com.usc.app.action.utils.ActionMessage;
import com.usc.app.action.utils.ResultMessage;
import com.usc.app.execption.GetExceptionDetails;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.obj.api.BeanFactoryConverter;
import com.usc.obj.api.impl.AppFileContext;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.jsonbean.ActionRequestJSONBean;
import com.usc.server.md.ItemInfo;
import com.usc.server.util.LoggerFactory;

public class DefaultEventProcessor
{
	private Object resultMap = new Object();

	private MultipartFile file;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	public MultipartFile getFile()
	{
		return this.file;
	}

	public void setFile(MultipartFile file)
	{
		this.file = file;
	}

	public Object getResultMap()
	{
		return resultMap;
	}

	public void setResultMap(Object obj)
	{
		if (obj == null)
		{
			this.resultMap = StandardResultTranslate.successfulOperation();
			return;
		}
		if (obj instanceof ResultMessage)
		{
			this.resultMap = (ResultMessage) obj;
			return;
		}
		if (obj instanceof Map)
		{
			this.resultMap = (Map) obj;
			return;
		}

		if (obj instanceof Optional)
		{
			Optional object = (Optional) obj;
			if (object.isPresent())
			{
				obj = object.get();
				setResultMap(obj);
			}
			return;
		}

		this.resultMap = new ActionMessage(true, null, StandardResultTranslate.translate("Action_Default"), null, obj);
	}

	public DefaultEventProcessor(MultipartFile file, HttpServletRequest request, HttpServletResponse response)
	{
		String queryParam = request.getParameter("values");
		ActionRequestJSONBean jsonBean = null;
		try
		{
			jsonBean = BeanFactoryConverter.getJsonBean(ActionRequestJSONBean.class, queryParam);
			if (jsonBean != null)
			{
				jsonBean.init();
				setFile(file);
				setRequest(request);
				setResponse(response);
				action(jsonBean);
			}
		} catch (Exception e)
		{
			setResultMap(StandardResultTranslate.getResult(false, GetExceptionDetails.details(e)));
			return;
		}

	}

	private void action(ActionRequestJSONBean jsonBean) throws Exception
	{

		String impl = jsonBean.getImplclass();
		inspect(impl);
		Object clazz = null;
		Object action = null;
		if (impl != null)
		{
			clazz = AppActionFactory.getAction(impl);
			if (clazz == null)
			{
				LoggerFactory.logError("未找到实现类：" + impl, new Throwable("No menu found : " + impl));
				setResultMap(StandardResultTranslate.getResult(false, "未找到实现类：：" + impl));
				return;
			}
			InspectionImplClass.init(clazz, jsonBean);
			execute((AppAction) clazz, jsonBean);
		} else
		{
			LoggerFactory.logError("未找到实现类：" + impl, new Throwable("No menu found : " + impl));
		}
	}

	private boolean inspect(String impl)
	{
		return true;

	}

	private void execute(AppAction action, ActionRequestJSONBean jsonBean)
	{
		ApplicationContext context = createContext(jsonBean);
		action.setApplicationContext(context);

		try
		{
			if (doBeforeAction(action))
			{
				return;
			}

			Object object = executeAction(action, context);

			doAfterAction(action);

			setResultMap(object);
		} catch (Exception e)
		{
			e.printStackTrace();
			setResultMap(StandardResultTranslate.getResult(false, e.getMessage()));
			return;
		} finally
		{
//			if (context instanceof AppFileContext)
//			{
//				ServletOutputStream outs;
//				try
//				{
//					outs = ((AppFileContext) context).getResponse().getOutputStream();
//					if (outs != null)
//					{
//						outs.flush();
//						outs.close();
//					}
//				} catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//
//			}
		}
	}

	private Object executeAction(AppAction action, ApplicationContext context) throws Exception
	{
		action.setApplicationContext(context);
		return action.action();
	}

	private boolean doBeforeAction(AppAction action)
	{
		// TODO Auto-generated method stub
		return false;
	}

	private void doAfterAction(AppAction action)
	{
		// TODO Auto-generated method stub

	}

	private ApplicationContext createContext(ActionRequestJSONBean jsonBean)
	{
		ApplicationContext applicationContext = new ApplicationContext(jsonBean);
		applicationContext.setContext();
		ItemInfo info = applicationContext.getItemInfo();
		if (info == null)
		{
			return applicationContext;
		} else
		{
			if (info.getType() == 1)
			{
				applicationContext = new AppFileContext(jsonBean, getFile());
			}
		}
		applicationContext.setServletRequest(getRequest());
		applicationContext.setServletResponse(getResponse());

		return applicationContext;
	}

}
