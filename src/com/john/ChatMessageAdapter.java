package com.john;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import com.john.R;
import com.john.bean.ChatMessage;
import com.john.bean.ChatMessage.Type;

public class ChatMessageAdapter extends BaseAdapter
{
	//LayoutInflater用来压栈布局文件,功能类似于在xml内部用findViewById检索控件，LayoutInflater是在layout文件夹下检索布局文件（xml文件）
	private LayoutInflater mInflater;
	//数据源
	private List<ChatMessage> mDatas;
	// 定义弱引用变量
	private WeakReference<Activity> weak; 
	private String msg="";

	public ChatMessageAdapter(Context context, List<ChatMessage> mDatas)
	{
		weak = new WeakReference<Activity>((Activity)context);
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * 如果item的式样都一样，只需复写上面4个方法，但这里有两种item布局——发出和收到，故要复写getItemViewType和getViewTypeCount
	 */
	@Override
	public int getItemViewType(int position)
	{
		ChatMessage chatMessage = mDatas.get(position);
		//收到的信息标为0，发出的信息标为1
		if (chatMessage.getType() == Type.INCOMING)
		{
			return 0;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount()
	{
		//总共2种item式样布局
		return 2;
	}

	/*
	 * 获取item布局
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ChatMessage chatMessage = mDatas.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			// 通过ItemType设置不同的布局
			if (getItemViewType(position) == 0)
			{
				//通过LayoutInflater检索item式样布局文件
				convertView = mInflater.inflate(R.layout.item_from_msg, parent,
						false);
				viewHolder = new ViewHolder();
				viewHolder.mDate = (TextView) convertView
						.findViewById(R.id.id_form_msg_date);
				viewHolder.mMsg = (Button) convertView
						.findViewById(R.id.id_from_msg_info);
			} else
			{
				convertView = mInflater.inflate(R.layout.item_to_msg, parent,
						false);
				viewHolder = new ViewHolder();
				viewHolder.mDate = (TextView) convertView
						.findViewById(R.id.id_to_msg_date);
				viewHolder.mMsg = (Button) convertView
						.findViewById(R.id.id_to_msg_info);
			}
			//Adapter类有个getView方法，可以使用setTag把查找的view缓存起来方便多次重用
			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 设置数据
		SimpleDateFormat df = new SimpleDateFormat("  yyyy-MM-dd HH:mm:ss  ");
		viewHolder.mDate.setText(df.format(chatMessage.getDate()));
		msg=chatMessage.getMsg();
		viewHolder.mMsg.setText(msg);
		viewHolder.mMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				Bundle bd=new Bundle();
				bd.putString("msg",((Button) v).getText().toString());
				intent.putExtras(bd);
				intent.setClass(weak.get().getBaseContext(),EditTextActivity.class);
				weak.get().startActivity(intent);
			}
		});
		return convertView;
	}

	/*
	 * 定义一个ViewHolder,将convertView的tag设置为ViewHolder，不为空是重新使用
	 * ViewHolder只是将需要缓存的那些view封装好，convertView的setTag才是将这些缓存起来供下次调用
	 */
	private final class ViewHolder
	{
		TextView mDate;
		Button mMsg;
		
	}
	/*
	 * 按对话框准跳EditTextActivity
	 */
	
		
	

}
