package com.usc.obj.api;

import java.util.List;

public interface UserDepartmentAndRoleProvider
{
	abstract String getUserDepartment(UserInfoProvider infoProvider);

	abstract List getUserRoles(UserInfoProvider infoProvider);
}
