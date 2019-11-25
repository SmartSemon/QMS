package com.usc.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractRelationAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.AppFileContext;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.type.file.FileObject;
import com.usc.obj.api.type.file.IFile;

public class CreateRelationObjAction extends AbstractRelationAction
{

	@Override
	public Object executeAction() throws Exception
	{
		Map newData = ActionParamParser.getFieldVaues(context.getItemInfo(), context.getItemPage(),
				context.getInitData());
		context.setInitData(newData);
		USCObject newObj = context.createObj(context.getItemNo());
		if (newObj != null)
		{
			Map relationData = GetRelationData.getData(getRoot(), newObj);
			ApplicationContext applicationContext = (ApplicationContext) context.cloneContext();
			applicationContext.setItemNo(getRelationShip().getRelationItem());
			applicationContext.setInitData(relationData);
			applicationContext.createObj(applicationContext.getItemNo());
			if (newObj instanceof IFile)
			{
				doFileAction(newObj);
			}
		}
		List<Map> list = new ArrayList<Map>();
		list.add(newObj.getFieldValues());
		return createSuccessful(list);
	}

	private boolean doFileAction(USCObject object)
	{
		AppFileContext fileContext = (AppFileContext) context;
		FileObject fileObject = (FileObject) object;
		return fileObject.upLoadFile(fileContext);
	}

}
