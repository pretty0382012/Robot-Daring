package com.sqllite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.john.bean.ChatMessage;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqlliteTools {
	public SqlliteTools(){}
	/*
	 * 创建表，如果已创建，则不重复创建
	 */
	public void CreateChatMessagetable(String tb,SQLiteDatabase db){
		db.execSQL("create table if not exists " +tb+
				"(_id integer primary key autoincrement," +
				"name text not null," +
				"msg text not null," +
				"type integer not null," +
				"date text not null," +
				"isread text not null)");	
	};
	public void CreateUsertable(String tb,SQLiteDatabase db){
		db.execSQL("create table if not exists " +tb+
				"(_id integer primary key autoincrement," +
				"username text not null)");	
	};
	public boolean InsertDataIntoNewDatebase(SQLiteDatabase db,String name,String msg,int type,String date,String isread){
		
	
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("msg", msg);
		values.put("type", type);
		values.put("date", date);
		values.put("isread", isread);
		long idrow=db.insert("newtb", null, values);
		if(idrow!=0){
			return true;
		}else{
			return false;
		}
	}
	public boolean InsertDataIntoOldDatebase(SQLiteDatabase db,String name,String msg,int type,String date,String isread){

		
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("msg", msg);
		values.put("type", type);
		values.put("date", date);
		values.put("isread", isread);
		long idrow=db.insert("oldtb", null, values);
		
		if(idrow!=0){
			return true;
		}else{
			return false;
		}
	}
public boolean InsertUsername(SQLiteDatabase db,String username){

		
		ContentValues values = new ContentValues();
		values.put("username",username);
		long idrow=db.insert("usernametb", null, values);
		
		if(idrow!=0){
			return true;
		}else{
			return false;
		}
	}
	public ArrayList<ChatMessage> ReadData(SQLiteDatabase db,String table,String column,String value){
		ArrayList<ChatMessage> ChatMessagelist = new ArrayList<ChatMessage>();
		
		Cursor c = db.rawQuery("select * from "+table+" where "+column+" like '%"+value+"%' order by _id asc", null);//读出表中数据并按时间排序
		if (c!=null){
			while(c.moveToNext()){
				    ChatMessage ChatMessage = new ChatMessage();
					ChatMessage.setName(c.getString(1));
					ChatMessage.setMsg(c.getString(2));
					ChatMessage.setType(c.getInt(3));
					try {
						ChatMessage.setDate( ConverToDate(c.getString(4)));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ChatMessagelist.add(ChatMessage);
			}
			c.close();
		}
		//db.close();
		return ChatMessagelist;
	}
	
	public ArrayList<ChatMessage> ReadData(SQLiteDatabase db,String table){
		ArrayList<ChatMessage> ChatMessagelist = new ArrayList<ChatMessage>();
		Cursor c = db.rawQuery("select * from "+table+" order by _id asc", null);//读出表中数据并按时间排序
		if (c!=null){
			while(c.moveToNext()){
				    ChatMessage ChatMessage = new ChatMessage();
				    ChatMessage.setName(c.getString(1));
					ChatMessage.setMsg(c.getString(2));
					ChatMessage.setType(c.getInt(3));
					try {
						ChatMessage.setDate( ConverToDate(c.getString(4)));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ChatMessagelist.add(ChatMessage);
			}
			c.close();
		}
		//db.close();
		return ChatMessagelist;
	}
	public String ReadUsername(SQLiteDatabase db,String table){
		String username="";
		
		Cursor c = db.rawQuery("select * from "+table, null);//c is a collection
		if (c!=null){
			while(c.moveToNext()){
				    username=c.getString(1);
			}
			c.close();
		}
		//db.close();
		return username;
	}
	public boolean ClearData(SQLiteDatabase db){
		db.execSQL("delete from usertb");
		Cursor c = db.rawQuery("select * from usertb", null);//c is a collection
		if (c==null){
			return true;
		}else{
			return false;
		}
	}
	/*
	 * 把字符串转为日期  
	 */
    public static Date ConverToDate(String strDate) throws Exception  
    {  
    	SimpleDateFormat df = new SimpleDateFormat("  yyyy-MM-dd HH:mm:ss  ");
        return df.parse(strDate);  
    }  
   
}
