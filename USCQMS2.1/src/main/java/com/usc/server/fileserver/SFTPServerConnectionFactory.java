package com.usc.server.fileserver;

import java.lang.reflect.Field;
import java.util.Properties;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.usc.util.ObjectHelperUtils;

public class SFTPServerConnectionFactory
{
	private static final Log LOG = LogFactory.getLog(SFTPServerConnectionFactory.class);

	private static String username = "admin";
	private static String password = "admin";
	private static String privateKey = "1";
	private static String ip = "192.168.2.139";
	private static int port = 22;

	private static final SFTPServerConnectionFactory SFTP_SERVER_CONNECTION_FACTORY = new SFTPServerConnectionFactory();
	private ChannelSftp sftp;
	private Session session;

	public SFTPServerConnectionFactory()
	{
	}

	public static FileServerConfig fileServerConfig;

	public static FileServerConfig getFileServerConfig()
	{
		return fileServerConfig;
	}

	public static void setFileServerConfig(FileServerConfig config)
	{
		fileServerConfig = config;
	}

	public static SFTPServerConnectionFactory getNewInstance()
	{
		return SFTP_SERVER_CONNECTION_FACTORY;
	}

	synchronized public ChannelSftp createConnection()
	{
		if (ObjectHelperUtils.isEmpty(sftp) || ObjectHelperUtils.isEmpty(session) || !sftp.isConnected()
				|| !session.isConnected())
		{
			com.jcraft.jsch.Logger logger = new SettleLogger();
			JSch jSch = new JSch();
			try
			{
				JSch.setLogger(logger);
				this.session = jSch.getSession("admin", "192.168.2.139", 22);
				if (!ObjectHelperUtils.isEmpty("admin"))
				{
					this.session.setPassword("admin");
				}

				Properties properties = new Properties();
				properties.put("StrictHostKeyChecking", "no");
				properties.put("userauth.gssapi-with-mic", "no");
//				properties.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
				this.session.setConfig(properties);
				this.session.setTimeout(30000);
				this.session.connect();

				Channel channel = this.session.openChannel("sftp");
				channel.connect();
				this.sftp = (ChannelSftp) channel;
				Class cl = ChannelSftp.class;
				Field field = cl.getDeclaredField("server_version");
				field.setAccessible(true);
				field.set(sftp, 2);
				sftp.setFilenameEncoding("UTF-8");
				LOG.info("----------文件资源服务器连接成功----------");
			} catch (JSchException | NoSuchFieldException | SecurityException | IllegalArgumentException
					| IllegalAccessException | SftpException e)
			{
				LOG.error("sftp登录失败，检测登录ip，端口号，用户名密码是否正确，错误信息为", e);
				return null;
			}
		}

		return this.sftp;

	}

	public void closeConnection()
	{
		if (this.sftp != null)
		{
			if (this.sftp.isConnected())
			{
				this.sftp.disconnect();
			}
		}
		if (this.session != null)
		{
			if (this.session.isConnected())
			{
				this.session.disconnect();
			}
		}
	}

	public class SettleLogger implements com.jcraft.jsch.Logger
	{
		public boolean isEnabled(int level)
		{
			return false;
		}

		public void log(int level, String msg)
		{
			System.out.println(msg);
		}
	}
}
