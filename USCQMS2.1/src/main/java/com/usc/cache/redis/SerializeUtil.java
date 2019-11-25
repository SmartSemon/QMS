package com.usc.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * ClassName: SerializeUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(序列化). <br/>
 * date: 2019年7月31日 下午4:22:48 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class SerializeUtil
{

	/**
	 * 序列化
	 *
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj)
	{
		byte[] bytes = null;
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try
		{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			bytes = baos.toByteArray();
			oos.close();
			baos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 反序列化
	 *
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes)
	{
		Object obj = null;
		try
		{
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		} catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		return obj;
	}

}
