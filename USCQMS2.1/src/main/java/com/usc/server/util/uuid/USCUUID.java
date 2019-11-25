package com.usc.server.util.uuid;

/**
 * 
 * <p>
 * Title: USCUUIDHexGenerator
 * </p>
 * 
 * <p>
 * Description: 采用UUID底层获取相关数据按照16进制生成有序的唯一ID
 * </p>
 * 
 * @author PuTianXiong
 * 
 * @date 2019年4月24日
 * 
 */
public class USCUUID
{

	public static String UUID()
	{
		return UUIDHexGenerator.generate();
	}
}
