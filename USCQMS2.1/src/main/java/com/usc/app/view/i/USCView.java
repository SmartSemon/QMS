package com.usc.app.view.i;

import com.usc.obj.jsonbean.JSONBean;
import com.usc.server.md.ModelClassView;

public interface USCView
{
	public abstract void initView(JSONBean jsonBean);

	public abstract ModelClassView getClassView();
}
