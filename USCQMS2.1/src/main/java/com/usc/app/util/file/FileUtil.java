package com.usc.app.util.file;

import java.io.File;
import java.io.IOException;

import org.springframework.util.FileSystemUtils;

public class FileUtil
{
	public static boolean deleteDirectory(String sPath)
	{
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator))
		{
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory())
		{
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			// 删除子文件
			if (files[i].isFile())
			{
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else
			{
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete())
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * 删除单个文件
	 *
	 * @param sPath 被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists())
		{
			file.delete();
			flag = true;
		}
		return flag;
	}

	public static boolean deleteRecursivelyFolder(String sPath)
	{
		File file = new File(sPath);
		return deleteRecursivelyFolder(file);
	}

	public static boolean deleteRecursivelyFolder(File folder)
	{
		return FileSystemUtils.deleteRecursively(folder);
	}

	public static void copyRecursively(File src, File dest) throws IOException
	{
		FileSystemUtils.copyRecursively(src.toPath(), dest.toPath());
	}
}
