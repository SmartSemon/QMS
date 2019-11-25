package com.usc.obj.api.type.file;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.impl.AppFileContext;
import com.usc.server.md.field.FieldNameInitConst;
import com.usc.server.util.SystemTime;

public class FileObject extends FileDBObj
{
	/**
	 *
	 */
	private static final long serialVersionUID = -531252898153222818L;

	public FileObject(String objType, HashMap<String, Object> map)
	{
		super(objType, map);
	}

	public boolean delete(InvokeContext context) throws Exception
	{
		return super.delete(context);
	}

	@Override
	public boolean upLoadFile(AppFileContext context)
	{
		if (context.getMultipartFile() == null)
		{
			return true;
		}
		String location = getLocation();
		MultipartFile file = context.getMultipartFile();
		File folder = FileObjUtil.getFolder(this, location);
		String folderPath = folder.getPath().replace("/", FileObjUtil.BACKSLASH);
		if (FileObjUtil.upLoad(context, file, folder.getPath(), this))
		{
			String nPath = folderPath.substring(folderPath.indexOf(location) + location.length());
			this.setFieldValue(FieldNameInitConst.FIELD_FLOCATION, nPath);
			this.setFieldValue(FieldNameInitConst.FIELD_FMTIME, SystemTime.getTimestamp());
			this.save(context);

			return true;
		}

		return false;

	}

	@Override
	public boolean downLoadFile(AppFileContext context)
	{
		try
		{
			return FileObjUtil.downloadFile(context, this, getLocation(), context.getServletRequest(),
					context.getServletResponse());
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean replaceLocationFile(AppFileContext context)
	{
		if (context.getMultipartFile() == null)
		{
			return this.save(context);
		}
		File serverFile = this.getServerLocationFile();
		boolean b = false;
		if (serverFile != null && serverFile.exists() && serverFile.isFile())
		{
			String fp = serverFile.getPath();
			File hisFile = new File(serverFile.getParent() + FileObjUtil.BACKSLASH + this.getID() + FileObjUtil._LINE
					+ SystemTime.getTimestamp().getTime()
					+ (fp.substring(fp.lastIndexOf(FileObjUtil.SOPT), fp.length())));
			b = FileObjUtil.replaceFileName(serverFile, hisFile);
		} else
		{
			b = true;
		}
		return b ? this.upLoadFile((AppFileContext) context) : false;
	}

	@Override
	public boolean hasFile()
	{
		return getServerLocationFile() != null;
	}

	@Override
	public File getServerLocationFile()
	{
		if (super.hasFile())
		{
			String id = this.getID();
			String flocation = this.getFileLocation();
			String ftype = this.getFileType();
			String serverFilePath = this.getLocation() + flocation + File.separator + id + ftype;
			File file = new File(serverFilePath);
			return file;
		}
		return null;

	}
}
