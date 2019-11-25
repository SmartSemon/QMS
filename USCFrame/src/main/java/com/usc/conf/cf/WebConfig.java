package com.usc.conf.cf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.usc.obj.api.type.task.TaskObject;

@ControllerAdvice
@Configuration
public class WebConfig implements WebMvcConfigurer
{
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
	{
		for (int i = converters.size() - 1; i >= 0; i--)
		{
			if (converters.get(i) instanceof MappingJackson2HttpMessageConverter)
			{
				converters.remove(i);
			}
		}
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();// 4
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, // 是否输出值为null的字段,默认为false,我们将它打开
				SerializerFeature.WriteNullListAsEmpty, // 将Collection类型字段的字段空值输出为[]
				SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null,输出为false,而非null
				SerializerFeature.WriteSlashAsSpecial, // 对斜杠’/’进行转义
				SerializerFeature.WriteDateUseDateFormat, // 全局序列化时间格式
				SerializerFeature.DisableCircularReferenceDetect // 禁用循环引用
//				SerializerFeature.WriteNullStringAsEmpty, 			// 将字符串类型字段的空值输出为空字符串
//				SerializerFeature.WriteNullNumberAsZero, 			// 将数值类型字段的空值输出为0
		);
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(TaskObject.class, "fieldValues");
		fastJsonConfig.setSerializeFilters(filter);
		List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		fastMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
		fastMediaTypes.add(MediaType.parseMediaType("text/plain;charset=utf-8"));
		fastMediaTypes.add(MediaType.parseMediaType("text/html;charset=utf-8"));
		fastMediaTypes.add(MediaType.parseMediaType("text/json;charset=utf-8"));
		converter.setSupportedMediaTypes(fastMediaTypes);
		converter.setFastJsonConfig(fastJsonConfig);
		converters.add(converter);
	}

}
