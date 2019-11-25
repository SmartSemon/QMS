package com.usc.obj.api.impl;

import java.util.List;
import java.util.Map;

import com.usc.obj.api.UserDepartmentAndRoleProvider;
import com.usc.obj.api.UserInfoProvider;
import com.usc.obj.api.bean.UserInformation;

public class UserClientInfo extends ClientInfo implements UserDepartmentAndRoleProvider
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public UserClientInfo()
	{
		super();
	}

	public UserClientInfo(Map<String, String> map)
	{
		super(map);
	}

	@Override
	public String getUserDepartment(UserInfoProvider infoProvider)
	{
		if (infoProvider != null)
		{
			UserInformation info = infoProvider.getUserInformation();
		}
		return null;
	}

	@Override
	public List getUserRoles(UserInfoProvider infoProvider)
	{
		if (infoProvider != null)
		{

		}
		return null;
	}

}
