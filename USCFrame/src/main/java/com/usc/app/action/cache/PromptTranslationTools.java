package com.usc.app.action.cache;

import java.io.FileNotFoundException;
import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.ResourceUtils;

import com.usc.app.util.PropertyFileUtil;
import com.usc.util.ObjectHelperUtils;

@CacheConfig(cacheNames = "Translation")
public class PromptTranslationTools
{
	public static String defaultMes = "";
	public static String task = "res/action/taskmes.properties";
	public static String fileAction = "res/action/taskmes.properties";

	@Cacheable(key = "#key")
	public static String translationMessage(String key, String... itemNo)
	{
		Map<Object, Object> mes = null;
		try
		{
			if (ObjectHelperUtils.isEmpty(itemNo))
			{
				mes = PropertyFileUtil.propertice(ResourceUtils
						.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "res/action/defaultaction.properties"));
			} else
			{
				mes = PropertyFileUtil.propertice(ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + task));
			}

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		return mes == null ? null : String.valueOf(mes.get(key));
	}

}
