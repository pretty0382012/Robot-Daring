package com.httpdownload.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileUtils {
	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}
	public FileUtils() {
		//�õ���ǰ�ⲿ�洢�豸��Ŀ¼����SDCARDĿ¼
		
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}
	/**
	 * ��SD���ϴ����ļ�
	 * @param filename
	 * @return File
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}
	
	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 */
	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	
	/**
	 * 
	 * ��һ��InputStream���������д�뵽SD����
	 * @param ָ���洢��ַpath��ָ���洢�ļ���filename������Դinputstream
	 * @return ���������ڱ������ļ�����file
	 */
	public File write2SDFromInput(String path,String fileName,InputStream is){
		File file = null;
		OutputStream output = null;
		try{
			//����Ŀ¼
			creatSDDir(path);
			//�������ļ�
			file = creatSDFile(path + fileName);
			//��öԸ��ļ��������������д������
			output = new FileOutputStream(file);
			//�����ֽ������СΪ4k����ÿ��д��4k��ѭ�����ã�֪��ȫ��д��
			byte buffer [] = new byte[4 * 1024];
			while((is.read(buffer)) != -1){
				output.write(buffer);
			}
			//д���ϰ���Ե���flush�����������ߵĻ���
			output.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				//���ǵùر������
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * ɾ���ļ����ļ���
	 * @param Ҫɾ����file
	 * @return �����Ƿ�ɾ���ɹ�
	 */
	public boolean deleteFoder(File file) {//ɾ��sdcard�ļ����ļ���
		 
        if (file.exists()) { // �ж��ļ��Ƿ����
                if (file.isFile()) { // �ж��Ƿ����ļ�
                        file.delete(); // delete()���� 
                } else if (file.isDirectory()) { // �����������һ��Ŀ¼
                        File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
                        if (files != null) {
                                for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
                                        deleteFoder(files[i]); // ��ÿ���ļ� ������������е���
                                }
                        }
                }
                boolean isSuccess = file.delete();
                if (!isSuccess) {
                        return false;
                }
        }
        return true;
        }
	/**
	 * ��sdcard��ȡͼƬbitmap
	 */
	public Bitmap getImageFromSD(String path){
		File mFile=new File(path);
		//�����ļ�����
        if (mFile.exists()) {
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            return bitmap;
        }
		return null;
	}
	
	 
	
	
}