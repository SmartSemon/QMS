package com.usc.app.sys.online;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.usc.app.util.UserInfoUtils;
import com.usc.obj.api.bean.UserInformation;
import com.usc.obj.api.type.user.UserInfoObject;
import com.usc.util.ObjectHelperUtils;

import lombok.Data;

public class OnlineUsers
{

	private static ConcurrentHashMap<String, OnlineUserInfo> onUsers = new ConcurrentHashMap<String, OnlineUserInfo>();
	private static ConcurrentHashMap<String, OnlineUserInfo> onClientUsers = new ConcurrentHashMap<String, OnlineUserInfo>();

	private static ConcurrentHashMap<String, OnlineUserInfo> getOnUsers()
	{
		return onUsers;
	}

	public static OnlineUserInfo getOnUser(String userName)
	{
		if (getOnUsers().containsKey(userName))
		{
			return getOnUsers().get(userName);
		}
		return null;
	}

	public static OnlineUserInfo getOnClientUser(String clientID)
	{
		if (onClientUsers.containsKey(clientID))
		{
			return onClientUsers.get(clientID);
		}
		return null;
	}

	public static void addOnlineUser(HttpServletRequest request, UserInformation userInfo)
	{
		if (request != null)
		{
			HttpSession session = request.getSession();
			session.setAttribute(UserInfoUtils.USER_SESSION_KEY, userInfo.getUserName());
			String ClientID = userInfo.getClientID();
			if (ClientID != null)
			{
				String ip = ClientInfoUtil.getIPAddress(request);
				String osBowser = ClientInfoUtil.getOsAndBrowserInfo(request);

				OnlineUserInfo onlineUserInfo = new OnlineUserInfo();
				onlineUserInfo.setIp(ip);
				onlineUserInfo.setOsBowser(osBowser);
				onlineUserInfo.setSsid(request.getSession().getId());
				onlineUserInfo.setLoginTime(new Timestamp(new Date().getTime()));
				onlineUserInfo.setUserName(userInfo.getUserName());
				onlineUserInfo.setEmployeeNo(userInfo.getEmployeeNo());
				onlineUserInfo.setEmployeeName(userInfo.getEmployeeName());
				onlineUserInfo.setTel(userInfo.getTel());
				onlineUserInfo.setMail(userInfo.getMail());
				onlineUserInfo.setDepartMentNo(userInfo.getDepartMentNo());
				onlineUserInfo.setDepartMentName(userInfo.getDepartMentName());
				onlineUserInfo.setLastTime(new Timestamp(System.currentTimeMillis()));
				onlineUserInfo.setUserInfoObject(UserInfoUtils.getUserInfoObject(userInfo.getUserName()));
				onUsers.put(userInfo.getUserName(), onlineUserInfo);
				onClientUsers.put(userInfo.getClientID(), onlineUserInfo);
				session.setAttribute(UserInfoUtils.USER_CLIENT_SESSION_KEY, ClientID);
			}
		}
	}

	public static boolean isOnline(String userName)
	{
		return getOnUsers().containsKey(userName);
	}

	public static void setOnlieUserLastTime(String userName)
	{
		OnlineUserInfo onlineUserInfo = onUsers.get(userName);
		onlineUserInfo.setLastTime(new Timestamp(new Date().getTime()));
		onUsers.put(userName, onlineUserInfo);
		onClientUsers.put(userName, onlineUserInfo);
	}

	public static List<OnlineUserInfo> getOnlineUsrs()
	{
		if (!ObjectHelperUtils.isEmpty(getOnUsers()))
		{
			List<OnlineUserInfo> infos = new ArrayList<OnlineUserInfo>();
			Map<String, OnlineUserInfo> users = getOnUsers();
			users.forEach((userName, userInfo) -> {
				Long now = new Date().getTime();
				Long lastTime = userInfo.getLastTime().getTime();
				Long x = now - lastTime;
				if (x > 1000 * 60 * 30)
				{
					removeUser(userName);
				} else
				{
					infos.add(userInfo);
				}
			});
			return infos;
		}
		return null;

	}

	@Data
	public static class OnlineUserInfo
	{
		private String userName;
		private String ip;
		private Timestamp loginTime;
		private String osBowser;
		private String employeeNo;
		private String employeeName;
		private String tel;
		private String mail;
		private String departMentNo;
		private String departMentName;
		private Timestamp lastTime;
		private UserInfoObject userInfoObject;

		private String ssid;

		public OnlineUserInfo()
		{
		}

		@Override
		public String toString()
		{
			return "OnlineUserInfo [userName=" + userName + ", ip=" + ip + ", loginTime=" + loginTime + ", osBowser="
					+ osBowser + ", employeeNo=" + employeeNo + ", employeeName=" + employeeName + ", tel=" + tel
					+ ", mail=" + mail + ", departMentNo=" + departMentNo + ", departMentName=" + departMentName + "]";
		}

	}

	public static void removeUser(String userName)
	{
		onUsers.remove(userName);
		onClientUsers.forEach((k, v) -> {
			if (v.getUserName().equals(userName))
			{
				onClientUsers.remove(k);
			}
		});
	}
}
