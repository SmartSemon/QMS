package com.usc.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.AppFileContext;
import com.usc.obj.api.type.file.IFile;
import com.usc.server.md.field.FieldNameInitConst;
import com.usc.util.ObjectHelperUtils;

public class BatchModifyAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		USCObject[] objects = context.getSelectObjs();
		boolean b = false;
		List list = new ArrayList<Map>();
		if (!ObjectHelperUtils.isEmpty(objects))
		{
			Map newData = ActionParamParser.getFieldVaues(context.getItemInfo(), context.getItemPage(),
					context.getInitData());

			if (objects.length == 1)
			{
				USCObject uscObject = context.getSelectedObj();
				uscObject.setFieldValues(newData);
				if (uscObject instanceof IFile)
				{
					IFile file = (IFile) uscObject;
					b = file.replaceLocationFile((AppFileContext) context);
				} else
				{
					b = uscObject.save(context);
				}
				if (b)
				{
					list.add(uscObject.getFieldValues());
				}
			}
			if (objects.length > 1)
			{
				USCObject object = context.getSelectedObj();
				if (object instanceof IFile)
				{
					removeFileFields(newData);
				}
				for (USCObject uscObject : objects)
				{
					uscObject.setFieldValues(newData);
					if (uscObject.save(context))
					{
						b = Boolean.TRUE;
						list.add(uscObject.getFieldValues());
					}
				}

			}
			if (!b)
			{
				return modifyFailed();
			}

		}
		return modifySuccessful(list);
	}

	private void removeFileFields(Map newData)
	{
		for (String field : FieldNameInitConst.getFileFields())
		{
			newData.remove(field);
		}
	}

	@Override
	public boolean disable() throws Exception
	{
		return false;
	}

}
