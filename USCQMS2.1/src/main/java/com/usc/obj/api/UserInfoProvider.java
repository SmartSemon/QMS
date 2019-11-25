package com.usc.obj.api;

import com.usc.obj.api.bean.UserInformation;

public interface UserInfoProvider
{
	public abstract UserInformation getUserInformation();

	public abstract UserInformation createUserInformation();

	public abstract UserInformation saveUserInformation();

	public abstract UserInformation deleteUserInformation();
}
