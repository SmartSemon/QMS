package com.usc.conf.cf;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig
{
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
	{
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
		redisTemplate.setValueSerializer(fastJsonRedisSerializer);
		redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		return redisTemplate;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory)
	{
		StringRedisTemplate template = new StringRedisTemplate(factory);
		FastJsonRedisSerializer<String> stringSerializer = new FastJsonRedisSerializer<String>(String.class);
		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setKeySerializer(stringSerializer);
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory)
	{
		RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionFactory);
		return builder.build();
	}

}
