package com.usc.obj.api.impl;

import org.springframework.web.multipart.MultipartFile;

import com.usc.obj.api.type.file.FileObject;
import com.usc.obj.jsonbean.JSONBean;

public class AppFileContext extends ApplicationContext
{
	protected MultipartFile multipartFile;
	protected String upOrDownLoadType;

	public AppFileContext(JSONBean bean, MultipartFile multipartFile)
	{
		super(bean);
		super.setContext();
		this.multipartFile = multipartFile;
		this.upOrDownLoadType = bean.getUpOrDownLoadType();
	}

	public MultipartFile getMultipartFile()
	{
		return this.multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile)
	{
		this.multipartFile = multipartFile;
	}

	public String getUpOrDownLoadType()
	{
		return upOrDownLoadType;
	}

	public void setUpOrDownLoadType(String upOrDownLoadType)
	{
		this.upOrDownLoadType = upOrDownLoadType;
	}

	public FileObject[] getSelectFileObjs()
	{
		return (FileObject[]) super.getSelectObjs();
	}

	public boolean fleExists()
	{
		if (getMultipartFile() != null)
		{
			return true;
		}
		return false;
	}
}
