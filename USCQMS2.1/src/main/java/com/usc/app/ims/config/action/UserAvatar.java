package com.usc.app.ims.config.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.impl.USCServerBeanProvider;
import com.usc.obj.util.USCObjectQueryHelper;

/**
 * @Author: lwp
 * @DATE: 2019/11/7 16:04
 * @Description:
 **/
@CrossOrigin
@RestController
@RequestMapping(value = "/src/user")
public class UserAvatar
{

	@PostMapping(value = "/uploadAvatar/{userId}")
	public boolean uploadAvatar(@PathVariable("userId") String userId, @RequestParam("avatar") MultipartFile file)
			throws IOException
	{
		if (file.isEmpty())
		{
			return false;
		}
		try
		{
			// 获取系统的”/“（系统不同斜杠会不一致，比如部署在linux中会报错）
			String separator = File.separator;
			// 获取项目根目录路径
			String dir = System.getProperty("user.dir");
			// 存储路径
			File pFile = new File(dir + separator + "avatar");
			// 创建一个文件
			String fileName = file.getOriginalFilename();
			File fileDir = new File(pFile, fileName);
			// 判断是否有该文件夹路径，没有则新建
			if (!pFile.exists())
			{
				pFile.mkdirs();
			}
			// 把接收的文件放入到文件中
			file.transferTo(fileDir.toPath());
			// 修改用户头像数据
			USCObject user = USCObjectQueryHelper.getObjectByID("SUSER", userId);
			ApplicationContext applicationContext = (ApplicationContext) USCServerBeanProvider.getContext("SUSER",
					user.getFieldValueToString("SNAME"));
			user.setFieldValue("avatar", fileName);
			user.save(applicationContext);
			return true;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@GetMapping(value = "/getAvatar/{userId}")
	public byte[] uploadAvatar(@PathVariable("userId") String userId) throws IOException
	{
		File file;
		String separator = File.separator;
		String dir = System.getProperty("user.dir");
		if ("系统通知".equals(userId))
		{
			file = new File(dir + separator + "avatar" + separator + "user.jpg");
		} else
		{
			USCObject user = USCObjectQueryHelper.getObjectByID("SUSER", userId);
			String fileName = user.getFieldValueToString("avatar");
			if (fileName != null)
			{
				file = new File(dir + separator + "avatar" + separator + fileName);
			} else
			{
				file = new File(dir + separator + "avatar" + separator + "user.jpg");
			}
		}
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes, 0, inputStream.available());
		return bytes;
	}

}
