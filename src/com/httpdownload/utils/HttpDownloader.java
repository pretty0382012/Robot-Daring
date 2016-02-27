package com.httpdownload.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class HttpDownloader {
	private URL url = null;

	/**
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容 1.创建一个URL对象
	 * 2.通过URL对象，创建一个HttpURLConnection对象 3.得到InputStram 4.从InputStream当中读取数据
	 * 注意：这个方法只是把网上的文本内容读到本地，并不涉及到存储到sdcard
	 * @param urlStr
	 * @return 文本内容String
	 */
	public String downloadtext(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			// 创建一个URL对象
			url = new URL(urlStr);
			// 创建一个Http连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// 使用IO流读取数据，inputstream是字节流，通过InputStreamReader()转化为字符流，字符流在通过BufferReader的readLine()方法一行行地读出
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 用来下载其他各种文件，并且存储到sdcard指定目录
	 * 根据url下载文件并自定义文件名称存在指定路径下 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
	 * @param covertheoldfile这个参数可以灵活适应多变的资源和基本不变的资源，对于多变的资源选择的覆盖，对于基本不变的资源选择不覆盖
	 */
	public int downFile(String urlStr, String path, String fileName,boolean covertheoldfile) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();
			//如果covertheoldfile==true，覆盖旧的文件，否则不再次下载

			if (fileUtils.isFileExist(path + fileName)&&!covertheoldfile) {
				return 1;
			} 
			else {
				inputStream = getInputStreamFromUrl(urlStr);
				File resultFile = fileUtils.write2SDFromInput(path, fileName,inputStream);
				if (resultFile == null) {
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlStr
	 * @return 输入流InputStream
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);
		// 由url字符串创建HttpURLConnection对象
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		// 通过HttpURLConnection对象获取输入流
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}

	/**
	 * 通过url获取图片的bitmap
	 * 
	 * @param urlstr
	 * @return Bitmap
	 */
	public Bitmap getHttpBitmap(String urlstr) {
		URL Url = null;
		Bitmap bitmap = null;

		try {

			Url = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) Url
					.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			// 获取输入流
			InputStream is = conn.getInputStream();
			// 将输入流转化为bitmap
			bitmap = BitmapFactory.decodeStream(is);
			// 记得关闭输入流
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
