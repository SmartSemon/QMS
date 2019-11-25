package com.usc.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.AppFileContext;
import com.usc.obj.api.type.file.FileObject;
import com.usc.obj.api.type.file.IFile;

public class CreateObjAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		if (disable())
		{
			Map newData = ActionParamParser.getFieldVaues(context.getItemInfo(), context.getItemPage(),
					context.getInitData());
			context.setInitData(newData);
			USCObject object = context.createObj(context.getItemNo());
			if (object == null)
			{
				if (context.getExtendInfo("CreateResult") != null)
				{
					return context.getExtendInfo("CreateResult");
				}
			} else
			{
				if (object instanceof IFile)
				{
					doFileAction(object);
				}
			}
			List<Map> list = new ArrayList<Map>();
			list.add(object.getFieldValues());
			return createSuccessful(list);
		}
		return failedOperation();

	}

	private void doFileAction(USCObject object)
	{
		AppFileContext fileContext = (AppFileContext) context;
		FileObject fileObject = (FileObject) object;
		fileObject.upLoadFile(fileContext);
	}

	@Override
	public boolean disable() throws Exception
	{
		return true;
	}

}
