package com.usc.app.action.mate;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.usc.app.action.AppActionFactory;
import com.usc.app.action.a.AbstractAction;
import com.usc.app.action.i.AppAction;
import com.usc.server.md.ItemMenu;

public class MenuEnableAuthAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		Object str = context.getExtendInfo("otherParam");
		if (str != null)
		{
			List<ItemMenu> ms = JSONArray.parseArray(String.valueOf(str), ItemMenu.class);
			for (ItemMenu m : ms)
			{
				AppAction clazz = AppActionFactory.getAction(m.getImplclass());
				if (clazz != null)
				{
					clazz.setApplicationContext(context);
					m.setDisabled(clazz.isEnabled());
				} else
				{
					m.setDisabled(flagFalse);
				}

			}
			return queryTrue(ms);
		}

		return failedOperation("item menus is null");
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
