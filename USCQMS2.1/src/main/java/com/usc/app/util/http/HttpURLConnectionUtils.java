package com.usc.app.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class HttpURLConnectionUtils
{
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String BOUNDARY = "----WebKitFormBoundaryzdoJf6rglgYbKLBw";

	public static Object sendGetRequest(String url, String param)
	{
		return param;

	}

	public static Object sendPostRequest(String urlString, String param, MultipartFile[] files)
	{
		try
		{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(POST);
			conn.setReadTimeout(3000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			conn.setRequestProperty("Charsert", "UTF-8");

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return param;

	}

	public static Object sendPostRequest(String urlString, Map<String, Object> param)
	{
		try
		{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(POST);
			conn.setReadTimeout(3000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Charsert", "UTF-8");
			PrintWriter writer = new PrintWriter(conn.getOutputStream());
			writer.print(param);
			writer.flush();
			writer.close();

			String result = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null)
			{
				result += line;
			}
			reader.close();
			return result;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;

	}
}
