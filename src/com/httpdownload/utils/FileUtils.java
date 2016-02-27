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
		//得到当前外部存储设备的目录，即SDCARD目录
		
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}
	/**
	 * 在SD卡上创建文件
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
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	
	/**
	 * 
	 * 将一个InputStream里面的数据写入到SD卡中
	 * @param 指定存储地址path，指定存储文件名filename，数据源inputstream
	 * @return 返回下载在本机的文件对象file
	 */
	public File write2SDFromInput(String path,String fileName,InputStream is){
		File file = null;
		OutputStream output = null;
		try{
			//创建目录
			creatSDDir(path);
			//创建空文件
			file = creatSDFile(path + fileName);
			//获得对该文件的输出流，用来写入内容
			output = new FileOutputStream(file);
			//缓存字节数组大小为4k，即每次写入4k，循环调用，知道全部写完
			byte buffer [] = new byte[4 * 1024];
			while((is.read(buffer)) != -1){
				output.write(buffer);
			}
			//写完后习惯性调用flush清空输出流工具的缓存
			output.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				//最后记得关闭输出流
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 删除文件或文件夹
	 * @param 要删除的file
	 * @return 返回是否删除成功
	 */
	public boolean deleteFoder(File file) {//删除sdcard文件或文件夹
		 
        if (file.exists()) { // 判断文件是否存在
                if (file.isFile()) { // 判断是否是文件
                        file.delete(); // delete()方法 
                } else if (file.isDirectory()) { // 否则如果它是一个目录
                        File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                        if (files != null) {
                                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                                        deleteFoder(files[i]); // 把每个文件 用这个方法进行迭代
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
	 * 从sdcard获取图片bitmap
	 */
	public Bitmap getImageFromSD(String path){
		File mFile=new File(path);
		//若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            return bitmap;
        }
		return null;
	}
	
	 
	
	
}