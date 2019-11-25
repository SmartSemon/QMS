package com.usc.obj.api.type.file;

import java.io.File;

import com.usc.app.util.PropertyFileUtil;
import com.usc.obj.api.impl.AppFileContext;

public interface IFile
{
	public boolean downLoadFile(AppFileContext context);

	public boolean upLoadFile(AppFileContext context);

	public boolean replaceLocationFile(AppFileContext context);

	public File getServerLocationFile();

	public boolean hasFile();

	default String getLocation()
	{
		return PropertyFileUtil.getApplicationPropertyValue("multipart.uploadpath");
	}

}
