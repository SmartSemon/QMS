package com.usc.app.us.user;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.query.rt.QueryReturnRequest;
import com.usc.app.sys.online.OnlineUsers;

public class GetOnlieUserInfoAction extends AbstractAction implements QueryReturnRequest
{

	@Override
	public Object executeAction() throws Exception
	{
		return queryTrue(OnlineUsers.getOnlineUsrs());
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
