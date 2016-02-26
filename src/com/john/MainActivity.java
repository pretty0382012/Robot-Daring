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

	//ListView展示消息
	private ListView mMsgsListView;
	//ListView要绑定适配器
	private ChatMessageAdapter mAdapter;
	//ListView需要数据源
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
			// 等待接收，子线程完成数据的返回
			ChatMessage fromMessge = (ChatMessage) msg.obj;
			mDatas.add(fromMessge);
			boolean flag=false;
			flag=sqt.InsertDataIntoNewDatebase( db,"小芳",fromMessge.getMsg(),fromMessge.getType(),df.format(fromMessge.getDate()),"无");
			Toast.makeText(MainActivity.this, "缓存到手机："+flag, Toast.LENGTH_SHORT).show();
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
		// 初始化事件
		initListener();
	}

	
	
	/*
	 * 初始化发送按钮
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
					Toast.makeText(MainActivity.this, "发送消息不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				ChatMessage toMessage = new ChatMessage();
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				
				mDatas.add(toMessage);
				boolean flag=false;
				flag=sqt.InsertDataIntoNewDatebase( db,"小芳",toMessage.getMsg(),toMessage.getType(),df.format(toMessage.getDate()),"无");
				Toast.makeText(MainActivity.this, "缓存到手机："+flag, Toast.LENGTH_SHORT).show();
				mAdapter.notifyDataSetChanged();
				mMsgsListView.setSelection(mDatas.size()-1);
				//按发送后清空输入框内容
				mInputEdt.setText("");
				
				//网络线程不能放在主线程，要新建一个子线程
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
	 * 初始化数据
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
		flag=sqt.InsertDataIntoNewDatebase( db,"Robot",cm.getMsg(),cm.getType(),df.format(cm.getDate()),"无");
		Toast.makeText(MainActivity.this, "缓存到手机："+flag, Toast.LENGTH_SHORT).show();
		//设定消息适配器,绑定数据源
		mAdapter = new ChatMessageAdapter(this, mDatas);
		//适配器绑定ListView
		mMsgsListView.setAdapter(mAdapter);
	}

	/*
	 * 初始化视图
	 */
	private void initView()
	{
		mMsgsListView = (ListView) findViewById(R.id.id_listview_msgs);
		mInputEdt = (EditText) findViewById(R.id.id_input_msg);
		mSendBtn = (Button) findViewById(R.id.id_send_msg);
	}
	/*
	 * 按照时间匹配问候语
	 */
	private String firststr(Date date){
		String s="";
		switch(date.getHours()){
		case 1:case 2:case 3:s="兔崽子，大半夜的不睡觉，当夜猫子真的好吗？失眠了？想我了？来来来，我们谈谈人生，咳咳！";break;
		case 4:case 5:case 6:s="亲爱的，起得真早啊。早起的鸟儿有虫吃！颁个“早起”大奖给你，拿好不谢";break;
		case 7:case 8:case 9:s="一天之计在于晨，新的一天，好好加油！";break;
		case 10:case 11:s="好好工作！好好学习！好好生活！";break;
		case 12:case 13:s="亲爱的，不要当拼命三郎哦，是时候吃午饭了！lunch time 走起";break;
		case 14:case 15:s="下午好，要不要来一杯下午茶呢，轻松一刻，充满能量，应对更多挑战，加油！";break;
		case 16:s="这个世界很精彩，这个世界很无奈。。。。";break;
		case 17:s="夕阳无限好，只是近黄昏，";break;
		case 18:case 19:s="亲爱的，晚饭吃了什么？";break;
		case 20:case 21:s="亲爱的，晚上好，我们来数星星吧！什么？没有星星？没有星星的夜里，我把香蕉送给你。。。咔咔";break;
		case 22:case 23:case 0:s="亲爱的，时间不早了，早睡早起身体好，晚安咯，么么哒！goodnight，good dream！";break;
		
		}
		return s;
		
	}

}
