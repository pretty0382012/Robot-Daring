package com.john;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.john.R;
import com.john.bean.ChatMessage;
import com.john.bean.ChatMessage.Type;
import com.john.utils.HttpUtils;
import com.sqllite.SqlliteTools;

public class MainActivity extends Activity
{

	//ListViewչʾ��Ϣ
	private ListView mMsgsListView;
	//ListViewҪ��������
	private ChatMessageAdapter mAdapter;
	//ListView��Ҫ����Դ
	private List<ChatMessage> mDatas;

	private EditText mInputEdt;
	private Button mSendBtn;

	private SQLiteDatabase db;
	private SqlliteTools sqt;
	private SimpleDateFormat df;
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			// �ȴ����գ����߳�������ݵķ���
			ChatMessage fromMessge = (ChatMessage) msg.obj;
			mDatas.add(fromMessge);
			boolean flag=false;
			flag=sqt.InsertDataIntoNewDatebase( db,"С��",fromMessge.getMsg(),fromMessge.getType(),df.format(fromMessge.getDate()),"��");
			Toast.makeText(MainActivity.this, "���浽�ֻ���"+flag, Toast.LENGTH_SHORT).show();
			mAdapter.notifyDataSetChanged();
			mMsgsListView.setSelection(mDatas.size()-1);
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		db=openOrCreateDatabase("user_db",MODE_PRIVATE, null);
		sqt=new SqlliteTools();
		sqt.CreateChatMessagetable("newtb", db);
		df = new SimpleDateFormat("  yyyy-MM-dd HH:mm:ss  ");
		
		initView();
		initDatas();
		// ��ʼ���¼�
		initListener();
	}

	
	
	/*
	 * ��ʼ�����Ͱ�ť
	 */
	private void initListener()
	{
		mSendBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final String toMsg = mInputEdt.getText().toString();
				if (TextUtils.isEmpty(toMsg))
				{
					Toast.makeText(MainActivity.this, "������Ϣ����Ϊ�գ�",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				ChatMessage toMessage = new ChatMessage();
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				
				mDatas.add(toMessage);
				boolean flag=false;
				flag=sqt.InsertDataIntoNewDatebase( db,"С��",toMessage.getMsg(),toMessage.getType(),df.format(toMessage.getDate()),"��");
				Toast.makeText(MainActivity.this, "���浽�ֻ���"+flag, Toast.LENGTH_SHORT).show();
				mAdapter.notifyDataSetChanged();
				mMsgsListView.setSelection(mDatas.size()-1);
				//�����ͺ�������������
				mInputEdt.setText("");
				
				//�����̲߳��ܷ������̣߳�Ҫ�½�һ�����߳�
				new Thread()
				{
					public void run()
					{
						ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
						Message m = Message.obtain();
						m.obj = fromMessage;
						mHandler.sendMessage(m);
					};
				}.start();

			}
		});
	}

	/*
	 * ��ʼ������
	 */
	private void initDatas()
	{
		mDatas = new ArrayList<ChatMessage>();
		mDatas=sqt.ReadData(db,"newtb");
		Date date =new Date();
		String s=firststr(date);
		ChatMessage cm=new ChatMessage(s, Type.INCOMING, new Date());
		mDatas.add(cm);
		boolean flag=false;
		flag=sqt.InsertDataIntoNewDatebase( db,"Robot",cm.getMsg(),cm.getType(),df.format(cm.getDate()),"��");
		Toast.makeText(MainActivity.this, "���浽�ֻ���"+flag, Toast.LENGTH_SHORT).show();
		//�趨��Ϣ������,������Դ
		mAdapter = new ChatMessageAdapter(this, mDatas);
		//��������ListView
		mMsgsListView.setAdapter(mAdapter);
	}

	/*
	 * ��ʼ����ͼ
	 */
	private void initView()
	{
		mMsgsListView = (ListView) findViewById(R.id.id_listview_msgs);
		mInputEdt = (EditText) findViewById(R.id.id_input_msg);
		mSendBtn = (Button) findViewById(R.id.id_send_msg);
	}
	/*
	 * ����ʱ��ƥ���ʺ���
	 */
	private String firststr(Date date){
		String s="";
		switch(date.getHours()){
		case 1:case 2:case 3:s="�����ӣ����ҹ�Ĳ�˯������ҹè����ĺ���ʧ���ˣ������ˣ�������������̸̸�������ȿȣ�";break;
		case 4:case 5:case 6:s="�װ��ģ�������簡�����������г�ԣ���������𡱴󽱸��㣬�úò�л";break;
		case 7:case 8:case 9:s="һ��֮�����ڳ����µ�һ�죬�úü��ͣ�";break;
		case 10:case 11:s="�úù������ú�ѧϰ���ú����";break;
		case 12:case 13:s="�װ��ģ���Ҫ��ƴ������Ŷ����ʱ����緹�ˣ�lunch time ����";break;
		case 14:case 15:s="����ã�Ҫ��Ҫ��һ��������أ�����һ�̣�����������Ӧ�Ը�����ս�����ͣ�";break;
		case 16:s="�������ܾ��ʣ������������Ρ�������";break;
		case 17:s="Ϧ�����޺ã�ֻ�ǽ��ƻ裬";break;
		case 18:case 19:s="�װ��ģ�������ʲô��";break;
		case 20:case 21:s="�װ��ģ����Ϻã������������ǰɣ�ʲô��û�����ǣ�û�����ǵ�ҹ��Ұ��㽶�͸��㡣��������";break;
		case 22:case 23:case 0:s="�װ��ģ�ʱ�䲻���ˣ���˯��������ã�������ôô�գ�goodnight��good dream��";break;
		
		}
		return s;
		
	}

}
