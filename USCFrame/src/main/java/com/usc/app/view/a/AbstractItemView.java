package com.usc.app.view.a;

import com.usc.app.action.mate.MateFactory;
import com.usc.app.query.a.AbstractQueryItemAction;
import com.usc.app.view.i.USCView;
import com.usc.obj.api.BeanFactoryConverter;
import com.usc.obj.jsonbean.JSONBean;
import com.usc.server.md.ModelClassView;
import com.usc.server.md.ModelClassViewTreeNode;
import com.usc.server.md.ModelQueryView;

public abstract class AbstractItemView extends AbstractQueryItemAction implements USCView
{

	protected ModelClassView classView;
	protected ModelClassViewTreeNode classViewNode;
	protected ModelQueryView queryView;

	public void initView(JSONBean json)
	{
		try
		{
			classViewNode = BeanFactoryConverter.getMapBean(ModelClassViewTreeNode.class, json.getClassViewNodeData());
			setClassView(MateFactory.getClassView(json.getViewNo()));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setClassView(ModelClassView classView)
	{
		this.classView = classView;
	}

	public ModelClassView getClassView()
	{
		return this.classView;
	}

}
