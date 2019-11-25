package com.usc.server.fileserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "sftp")
@PropertySource(value = "config/fileserver.properties")
public class FileServerConfig
{
	private String open;
	private String ip;
	private int port;

	private String username;
	private String password;
	private String privateKey;

	private int downloadSleep;
	private int downloadRetry;
	private int uploadSleep;
	private int uploadRettry;

}
