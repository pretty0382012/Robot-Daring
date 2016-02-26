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
	//LayoutInflater����ѹջ�����ļ�,������������xml�ڲ���findViewById�����ؼ���LayoutInflater����layout�ļ����¼��������ļ���xml�ļ���
	private LayoutInflater mInflater;
	//����Դ
	private List<ChatMessage> mDatas;
	// ���������ñ���
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
	 * ���item��ʽ����һ����ֻ�踴д����4��������������������item���֡����������յ�����Ҫ��дgetItemViewType��getViewTypeCount
	 */
	@Override
	public int getItemViewType(int position)
	{
		ChatMessage chatMessage = mDatas.get(position);
		//�յ�����Ϣ��Ϊ0����������Ϣ��Ϊ1
		if (chatMessage.getType() == Type.INCOMING)
		{
			return 0;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount()
	{
		//�ܹ�2��itemʽ������
		return 2;
	}

	/*
	 * ��ȡitem����
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ChatMessage chatMessage = mDatas.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			// ͨ��ItemType���ò�ͬ�Ĳ���
			if (getItemViewType(position) == 0)
			{
				//ͨ��LayoutInflater����itemʽ�������ļ�
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
			//Adapter���и�getView����������ʹ��setTag�Ѳ��ҵ�view������������������
			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// ��������
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
	 * ����һ��ViewHolder,��convertView��tag����ΪViewHolder����Ϊ��������ʹ��
	 * ViewHolderֻ�ǽ���Ҫ�������Щview��װ�ã�convertView��setTag���ǽ���Щ�����������´ε���
	 */
	private final class ViewHolder
	{
		TextView mDate;
		Button mMsg;
		
	}
	/*
	 * ���Ի���׼��EditTextActivity
	 */
	
		
	

}
