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
	 * ����URL�����ļ���ǰ��������ļ����е��������ı��������ķ���ֵ�����ļ����е����� 1.����һ��URL����
	 * 2.ͨ��URL���󣬴���һ��HttpURLConnection���� 3.�õ�InputStram 4.��InputStream���ж�ȡ����
	 * ע�⣺�������ֻ�ǰ����ϵ��ı����ݶ������أ������漰���洢��sdcard
	 * @param urlStr
	 * @return �ı�����String
	 */
	public String downloadtext(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			// ����һ��URL����
			url = new URL(urlStr);
			// ����һ��Http����
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// ʹ��IO����ȡ���ݣ�inputstream���ֽ�����ͨ��InputStreamReader()ת��Ϊ�ַ������ַ�����ͨ��BufferReader��readLine()����һ���еض���
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
	 * �����������������ļ������Ҵ洢��sdcardָ��Ŀ¼
	 * ����url�����ļ����Զ����ļ����ƴ���ָ��·���� �ú����������� -1�����������ļ����� 0�����������ļ��ɹ� 1�������ļ��Ѿ�����
	 * @param covertheoldfile����������������Ӧ������Դ�ͻ����������Դ�����ڶ�����Դѡ��ĸ��ǣ����ڻ����������Դѡ�񲻸���
	 */
	public int downFile(String urlStr, String path, String fileName,boolean covertheoldfile) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();
			//���covertheoldfile==true�����Ǿɵ��ļ��������ٴ�����

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
	 * ����URL�õ�������
	 * 
	 * @param urlStr
	 * @return ������InputStream
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);
		// ��url�ַ�������HttpURLConnection����
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		// ͨ��HttpURLConnection�����ȡ������
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}

	/**
	 * ͨ��url��ȡͼƬ��bitmap
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
			// ��ȡ������
			InputStream is = conn.getInputStream();
			// ��������ת��Ϊbitmap
			bitmap = BitmapFactory.decodeStream(is);
			// �ǵùر�������
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
