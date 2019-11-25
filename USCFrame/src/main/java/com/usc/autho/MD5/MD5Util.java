package com.usc.autho.MD5;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class MD5Util
{
	public static String getMD5Ciphertext(String str)
	{

		try
		{
			String ciphertext = MD5Utils.getEncryptedPwd(str);
			return ciphertext;
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static boolean validMD5Legitimacy(String string, String salt) throws Exception
	{
		return MD5Utils.validCiphertext(MD5Utils.getMd5Encoder(string), salt);

	}
}
