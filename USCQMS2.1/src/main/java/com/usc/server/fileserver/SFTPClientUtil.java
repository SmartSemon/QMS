package com.usc.server.fileserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.usc.app.execption.ThrowRemoteException;
import com.usc.util.ObjectHelperUtils;

import lombok.Data;

@Data

public class SFTPClientUtil
{
	private static final Log LOG = LogFactory.getLog(SFTPClientUtil.class);
	private static int downloadSleep;
	private static int downloadRetry;
	private static int uploadSleep = 100;
	private static int uploadRettry = 3;
	public static final Log logger = LogFactory.getLog(SFTPClientUtil.class);

	public synchronized static boolean uploadFile(String directory, String fileName, InputStream inputStream)
	{
		boolean result = false;
		Integer i = 0;
		while (!result)
		{
			SFTPServerConnectionFactory sftpServerConnectionFactory = SFTPServerConnectionFactory.getNewInstance();
			ChannelSftp sftp = sftpServerConnectionFactory.createConnection();
			try
			{
				sftp.cd(directory);
			} catch (SftpException e)
			{
				LOG.info("SFTP文件目录不存在，开始创建文件目录：" + directory);
				createDirectory(sftp, directory);
			}
			try
			{
				sftp.put(inputStream, fileName);
				if (i == 0)
				{
					LOG.info("sftp文件上传成功，ftp路径:" + directory + ",文件名称:" + fileName);
				} else
				{
					LOG.info("sftp重试文件上传成功，ftp路径:" + directory + ",文件名称:" + fileName);
				}
				result = true;
			} catch (SftpException e)
			{
				i++;
				LOG.error("sftp文件上传失败，重试中。。。第" + i + "次，错误信息" + e.getMessage());
				if (i > uploadRettry)
				{
					LOG.error("sftp文件上传失败，超过重试次数结束重试", e);
					return result;
				}
				try
				{
					TimeUnit.MILLISECONDS.sleep(uploadSleep);
				} catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}

				e.printStackTrace();
			} finally
			{
				if (inputStream != null)
				{
					try
					{
						inputStream.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				closeSFTPConnection(sftpServerConnectionFactory, sftp);
			}

		}
		return result;

	}

	public synchronized static boolean uploadFile(String basePath, String directory, File file)
			throws FileNotFoundException
	{
		return uploadFile(directory, file.getName(), new FileInputStream(file));
	}

	public static boolean downloadFile(String directory, String targetFileName, String outFileName,
			OutputStream outputStream)
	{
		SFTPServerConnectionFactory sftpServerConnectionFactory = SFTPServerConnectionFactory.getNewInstance();
		ChannelSftp sftp = sftpServerConnectionFactory.createConnection();
		if (sftp != null)
		{
			try
			{
				sftp.cd(directory.replace("\\", "/"));
				sftp.get(targetFileName, outputStream);
			} catch (SftpException e)
			{
				ThrowRemoteException.throwExceptionDetails(e);
			} finally
			{
				if (outputStream != null)
				{
					try
					{
						outputStream.flush();
						outputStream.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				closeSFTPConnection(sftpServerConnectionFactory, sftp);
			}
		}
		return true;
	}

	private static void closeSFTPConnection(SFTPServerConnectionFactory sftpServerConnectionFactory, ChannelSftp sftp)
	{
		if (sftp != null)
		{
			sftpServerConnectionFactory.closeConnection();
		}

	}

	public synchronized static boolean createDirectory(ChannelSftp sftp, String directory)
	{
		boolean ex = false;
		if (ObjectHelperUtils.isEmpty(directory))
		{
			LOG.error("eorrCode:F0001", new NullPointerException("String directory is null"));
		} else
		{
			String[] dirs = directory.replace("\\", "/").split("/");
			String tempDirectory = "";
			for (String dir : dirs)
			{
				if (ObjectHelperUtils.isEmpty(dir))
				{
					continue;
				}
				tempDirectory += "/" + dir;
				try
				{
					sftp.cd(tempDirectory);
					ex = true;
				} catch (SftpException e)
				{
					try
					{
						sftp.mkdir(tempDirectory);
						ex = true;
					} catch (SftpException e1)
					{
						ex = false;
						LOG.error("SFTP文件目录创建失败", e1);
					}
				}
			}
		}
		return ex;

	}
}
