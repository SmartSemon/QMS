package com.usc.obj.api.type.file;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.api.impl.AppFileContext;
import com.usc.obj.api.type.AbstractDBObj;
import com.usc.server.md.field.FieldNameInitConst;
import com.usc.util.ObjectHelperUtils;

public class FileDBObj extends AbstractDBObj implements IFile
{

	/**
	 *
	 */
	private static final long serialVersionUID = -6872657644557888457L;

	public FileDBObj(String objType, HashMap<String, Object> map)
	{
		super(objType, map);
	}

	public boolean downLoadFile(AppFileContext fileContext)
	{
		return true;
	}

	public boolean upLoadFile(AppFileContext fileContext)
	{
		return true;
	}

	public boolean delete(InvokeContext context) throws Exception
	{
		if (!isDeleteAble(context))
		{
			return false;
		}
		boolean b = false;
		if (!isDeleted(context))
		{
			USCObjectAction objectAction = context.getActionObjType();
			objectAction.setCurrObj(this);
			b = objectAction.delete(context);
			if (b)
			{
				setObj2Deleted();
			}
		}

		return b;
	}

	public String getFileName()
	{
		return this.getFieldValueToString(FieldNameInitConst.FIELD_FNAME);
	}

	public String getFileType()
	{
		return this.getFieldValueToString(FieldNameInitConst.FIELD_FTYPE);
	}

	public String getFileSize()
	{
		return this.getFieldValueToString(FieldNameInitConst.FIELD_FSIZE);
	}

	public Date getFileModifyTime()
	{
		return (Date) this.getFieldValue(FieldNameInitConst.FIELD_FMTIME);
	}

	public String getFileLocation()
	{
		return this.getFieldValueToString(FieldNameInitConst.FIELD_FLOCATION);
	}

	@Override
	public boolean hasFile()
	{
		return !ObjectHelperUtils.isEmpty(getFileLocation());
	}

	@Override
	public File getServerLocationFile()
	{
		if (!hasFile())
		{
			return null;
		}
		return null;
	}

	@Override
	public boolean replaceLocationFile(AppFileContext context)
	{
		return false;
	}

}
