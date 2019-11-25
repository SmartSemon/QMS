package com.usc.app.us.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.sys.online.OnlineUsers;
import com.usc.app.util.BeanConverter;
import com.usc.app.util.UserInfoUtils;
import com.usc.app.util.tran.FormatPromptInformation;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.bean.UserInformation;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.type.GeneralObject;

@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class PersonalInformationMaintenance
{
	@GetMapping("/getInfo")
	public Object queryUPinfo(HttpServletRequest request, HttpServletResponse response)
	{
		String clientID = request.getHeader("clientID");
		if (clientID != null)
		{
			HashMap<String, Object> userMap = UserInfoUtils.getUserInfoMap(clientID);
			String userName = (String) userMap.get("SNAME");
			UserInformation information = UserInfoUtils.getUserInformation(userName);
			List<UserInformation> dataList = new ArrayList<UserInformation>();
			dataList.add(information);
			return StandardResultTranslate.getQueryResult(true, "Action_Query", dataList);

		}
		return StandardResultTranslate.getResult(FormatPromptInformation.getAuthorMsg("Has_WKClient"), false);

	}

	@GetMapping("/getOnlie")
	public Object queryOnlie(HttpServletRequest request, HttpServletResponse response)
	{
//		String clientID = request.getHeader("clientID");
//		if (clientID != null)
//		{
//			Map userMap = UserInfoUtils.getUserInfoMap(clientID);
//			String userName = (String) userMap.get("SNAME");
//			UserInformation information = UserInfoUtils.getUserInformation(userName);
//			List<UserInformation> dataList = new ArrayList<UserInformation>();
//			dataList.add(information);
//			return StandardResultTranslate.getQueryResult(true, "Action_Query", dataList);
//
//		}

		return StandardResultTranslate.getResult("Action_Query_1", OnlineUsers.getOnlineUsrs());

	}

	@PostMapping("/modifiyInfo")
	public Object modifiyInfo(@RequestBody(required = true) String param, HttpServletRequest request,
			HttpServletResponse response)
	{
		String clientID = request.getHeader("clientID");
		if (clientID != null)
		{
			UserInformation userInformation = BeanConverter.toJavaBean(UserInformation.class,
					JSONObject.parseObject(param));
			ApplicationContext context = new ApplicationContext("SUSER", userInformation.getUserName());
//			context.setUserName(userInformation.getUserName());
			updateUserInfo(userInformation, context);
			updateEmployeeInfo(userInformation, context);

			List<UserInformation> dataList = new ArrayList<UserInformation>();
			dataList.add(userInformation);
			return StandardResultTranslate.getQueryResult(true, "Action_Update", dataList);

		}
		return StandardResultTranslate.getResult(FormatPromptInformation.getAuthorMsg("Has_WKClient"), false);

	}

	private void updateUserInfo(UserInformation userInformation, ApplicationContext context)
	{
		UserInformation user = UserInfoUtils.getUserInformation(userInformation.getUserName());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", user.getUserID());
		map.put("SNAME", user.getUserName());
		USCObject object = new GeneralObject("SUSER", map);
		context.setItemNo("SUSER");
		context.setCurrObj(object);
		object.setFieldValue("remark", userInformation.getUserRemark());
		object.save(context);
		user.setUserRemark(userInformation.getUserRemark());
		UserInfoUtils.putUserInfomation(user);
	}

	private void updateEmployeeInfo(UserInformation userInformation, ApplicationContext context)
	{
		UserInformation user = UserInfoUtils.getUserInformation(userInformation.getUserName());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", user.getEmployeeID());
		map.put("SUNO", user.getEmployeeNo());
		map.put("SUNAME", user.getEmployeeName());
		USCObject object = new GeneralObject("SPERSONNEL", map);
		context.setItemNo("SPERSONNEL");
		context.setCurrObj(object);
		object.setFieldValue("SUTEL", userInformation.getTel());
		object.setFieldValue("SAX", userInformation.getSax());
		object.setFieldValue("MAIL", userInformation.getMail());
		object.setFieldValue("AGE", userInformation.getAge());
		object.setFieldValue("IDCARD", userInformation.getIdCard());
		object.setFieldValue("WKSTATE", userInformation.getWkSate());
		object.setFieldValue("remark", userInformation.getEmployeeRemark());
		object.save(context);
		user.setTel(userInformation.getTel());
		user.setSax(userInformation.getSax());
		user.setMail(userInformation.getMail());
		user.setAge(userInformation.getAge());
		user.setIdCard(userInformation.getIdCard());
		user.setWkSate(userInformation.getWkSate());
		user.setEmployeeRemark(userInformation.getEmployeeRemark());
		UserInfoUtils.putUserInfomation(user);

	}
}
