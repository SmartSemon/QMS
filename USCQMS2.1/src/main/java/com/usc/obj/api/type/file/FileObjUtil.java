package com.usc.obj.api.type.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.multipart.MultipartFile;

import com.usc.app.util.servlet.ServerletUtils;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.impl.USCServerBeanProvider;
import com.usc.server.md.field.FieldNameInitConst;
import com.usc.server.syslog.LOGActionEnum;
import com.usc.server.util.LoggerFactory;
import com.usc.util.ObjectHelperUtils;

public class FileObjUtil
{
	public final static String SOPT = ".";
	public final static String BACKSLASH = "\\";
	public final static String _LINE = "_";

	public static File getFolder(FileObject object, String location)
	{
		Date date = new Date();
		DateFormatter formatter = new DateFormatter("yyyy-MM-dd");
		String LocalDate = formatter.print(date, Locale.getDefault());
		String folderPath = location + File.separator + object.getItemNo() + File.separator + LocalDate;
		File folder = new File(folderPath);
		if (!folder.exists())
		{
			folder.mkdirs();
		}
		return folder;
	}

	public static boolean upLoad(ApplicationContext context, MultipartFile file, String location, FileObject fileObject)
	{

		try
		{
			if (upLoad(fileObject, file, location))
			{

				USCServerBeanProvider.getSystemLogger().writeFileLog(context, LOGActionEnum.UL, fileObject);
				return true;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private static boolean upLoad(FileObject fileObject, MultipartFile file, String folderPath) throws IOException
	{
		if (fileObject == null || file == null)
		{
			return true;
		}
		String path = getInFilePath(fileObject, file, folderPath);
//		Date date = new Date();
//		DateFormatter formatter = new DateFormatter("yyyy-MM-dd");
//		String LocalDate = formatter.print(date, Locale.getDefault());
//			return SFTPClientUtil.uploadFile(fileObject.getItemNo() + "/" + LocalDate,
//					path.substring(path.lastIndexOf("\\") + 1, path.length()), file.getInputStream());
		if (copyFile(file, new File(path)))
		{
			FileInputStream ins = null;
			try
			{
				ins = new FileInputStream(new File(path));
				String fSize = getFileSize(ins);
				fileObject.setFieldValue(FieldNameInitConst.FIELD_FSIZE, fSize);
				return true;
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					ins.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static String getInFilePath(FileObject fileObject, MultipartFile file, String folder)
	{
		String fid = fileObject.getID();
		String ofname = file.getOriginalFilename();
		int indx = ofname.lastIndexOf(SOPT);
		String fileName = Objects.requireNonNull(ofname).substring(0, indx);
		String prefix = ofname.substring(indx);
		fileObject.setFieldValue(FieldNameInitConst.FIELD_FTYPE, prefix);
		fileObject.setFieldValue(FieldNameInitConst.FIELD_FNAME, fileName);
		String path = folder + File.separator + fid + prefix;
		return path;
	}

	public static boolean copyFile(MultipartFile file, File saveFile)
	{
		InputStream inputStream = null;
		try
		{
			inputStream = file.getInputStream();
			Path path = saveFile.toPath();
			copyFile(inputStream, path);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public static Long copyFile(InputStream inputStream, Path path) throws IOException
	{
		return Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
	}

	public static String getFileSize(FileInputStream inputStream) throws IOException
	{
		int accuracy = 3;
		BigDecimal b1 = new BigDecimal(1024);
		FileChannel channel = inputStream.getChannel();
		BigDecimal fileSize = new BigDecimal(channel.size());
		BigDecimal b = fileSize.divide(b1, accuracy, RoundingMode.HALF_UP);
		if (b.doubleValue() < 1)
		{
			return fileSize + " byte";
		}
		BigDecimal mb = b.divide(b1, accuracy, RoundingMode.HALF_UP);
		if (mb.doubleValue() < 1)
		{
			return b + " KB";
		}
		BigDecimal gb = mb.divide(b1, accuracy, RoundingMode.HALF_UP);
		if (gb.doubleValue() < 1)
		{
			return mb + " MB";
		} else
		{
			return gb + " GB";
		}
	}

	public static boolean downloadFile(ApplicationContext context, FileObject fileObject, String locaton,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
	{
		String fileName = fileObject.getFileName();
		String ftype = fileObject.getFileType();
		String outFileName = fileName + ftype;
		File file = fileObject.getServerLocationFile();
//		boolean b = false;
//
//		if (SFTPServerConnectionFactory.getFileServerConfig().getOpen().equals("true"))
//		{
//			b = doDownLoadSFTP(fileObject, request, response);
//		} else
//		{
//			b = doDownLoad(file, outFileName, request, response);
//		}
//		if (b)
//		{
//			USCServerBeanProvider.getSystemLogger().writeFileLog(context, LOGActionEnum.DL, fileObject);
//		}
		return doDownLoad(file, outFileName, request, response);
	}

	public static boolean doDownLoadSFTP(FileObject fileObject, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException
	{
		String fileName = fileObject.getFileName();
		String ftype = fileObject.getFileType();
		String taget = fileObject.getID() + ftype;
		String outFileName = fileName + ftype;
		ServerletUtils.setFileResponse(response, outFileName);
//		try
//		{
//			SFTPClientUtil.downloadFile(fileObject.getFileLocation(), taget, outFileName, response.getOutputStream());
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		return false;

	}

	public static boolean doDownLoad(File file, String outFileName, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException
	{
		if (file.exists())
		{
			ServerletUtils.setFileResponse(response, outFileName);

			InputStream fis = null;
			OutputStream outs = null;
			try
			{
				fis = new FileInputStream(file);

				outs = response.getOutputStream();

				byte[] buffer = new byte[fis.available()];
				int readTmp = 0;
				while ((readTmp = fis.read(buffer)) != -1)
				{
					outs.write(buffer, 0, readTmp);
				}

				LoggerFactory.logInfo("Download the File " + outFileName + " successfully!");
				return true;
			} catch (Exception e)
			{
				LoggerFactory.logInfo("Download the File " + outFileName + " failed!");
			} finally
			{
				try
				{
					if (outs != null)
					{
						outs.flush();
						outs.close();
					}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				if (fis != null)
				{
					try
					{
						fis.close();
						assert outs != null;
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				if (fis != null)
				{
					try
					{
						fis.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}

		}
		return false;
	}

	public static File getTmpFile()
	{
		String tmpid = UUID.randomUUID().toString().replace("-", "");
		String tmpPath = System.getProperty("user.dir") + File.separator + "work/file/tmp/" + tmpid;
		File tmpFile = new File(tmpPath);
		if (!tmpFile.exists())
		{
			tmpFile.mkdirs();
		}
		return tmpFile;
	}

	public static boolean replaceFileName(File hisFile, File newFile)
	{
		if (hisFile == null || newFile == null)
			return false;
		if (!hisFile.exists())
			return false;
		if (!hisFile.isFile())
			return false;
		if (newFile.exists())
		{
			newFile.delete();
		}
		if (hisFile.getPath().equals(newFile.getPath()))
		{
			return true;
		}
		return hisFile.renameTo(newFile);

	}

	public static boolean replaceFileName(final File file, final String newName)
	{
		if (file == null)
			return false;
		if (!file.exists())
			return false;
		if (ObjectHelperUtils.isEmpty(newName))
			return false;
		if (newName.trim().contains(" "))
			return false;
		if (newName.equals(file.getName()))
			return true;
		File newFile = new File(file.getParent() + File.separator + newName);
		return !newFile.exists() && replaceFileName(file, newFile);
	}

}
