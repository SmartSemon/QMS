package com.usc.app.action.editor.codegor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;

public class AcquisitionCodeAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject[] objects = context.getSelectObjs();
		if (objects == null)
		{
			return failedOperation();
		}
		CodeGenerator generator = new CodeGenerator(context);
		generator.generator(objects);

		Map<String, Object> map = successfulOperation();
		if (generator.getExhaust())
		{
			int i = 0;
			String code = generator.getResultCode();
			while (code == null)
			{
				i++;
				generator.generator(objects);
				code = generator.getResultCode();
				if (i == 2000)
				{
					break;
				}
			}
			if (code == null)
			{
				map.put("flag", false);
				map.put("info", "取码失败");
				return map;
			}
			List list = new ArrayList<>();
			list.add(code);
			map.put("dataList", list);

		} else
		{
			map.put("flag", false);
			map.put("info", "编码用完，请联系管理员");
		}

		return map;
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
