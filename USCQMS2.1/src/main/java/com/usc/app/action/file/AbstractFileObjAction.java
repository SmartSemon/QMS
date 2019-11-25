package com.usc.app.action.file;

import org.springframework.core.io.InputStreamSource;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.impl.AppFileContext;

public abstract class AbstractFileObjAction extends AbstractAction
{
	private InputStreamSource MFile;

	public String getFileType()
	{
		return ((AppFileContext) context).getUpOrDownLoadType();
	}

	public InputStreamSource getMFile()
	{
		return ((AppFileContext) context).getMultipartFile();
	}

	public boolean fileExists()
	{
		if (MFile == null)
		{
			return false;
		}
		return true;
	}

}
