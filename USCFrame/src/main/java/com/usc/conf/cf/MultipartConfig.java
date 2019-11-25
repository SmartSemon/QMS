package com.usc.conf.cf;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
public class MultipartConfig
{

	@Value("${fileConfig.Multipart.MaxFileSize}")
	private String maxFileSzie;

	@Value("${fileConfig.Multipart.MaxRequestSize}")
	private String maxRequestSzie;

	@Bean
	public MultipartConfigElement configElement()
	{
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(maxFileSzie);
		factory.setMaxRequestSize(maxRequestSzie);
		String location = System.getProperty("user.dir") + "/work/file/tmp";
		File tmpFile = new File(location);
		if (!tmpFile.exists())
		{
			tmpFile.mkdirs();
		}
		factory.setLocation(location);
		return factory.createMultipartConfig();
	}

}
