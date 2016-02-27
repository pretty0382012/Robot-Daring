package com.uploadimage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.httpdownload.utils.FileUtils;
import com.httpdownload.utils.HttpDownloader;
import com.john.R;
import com.uploadimage.util.NetWorkUtil;

public class ChangeLogoActivity extends Activity implements OnClickListener{
	
	private Button chosePic,uploadPic;
	private ImageView imageView;
	private Intent dataIntent = null;
	private final String url = "http://10.108.165.119:8080/ResponseTest/ResponseServlet";
	private long userid=0;
	private final String imageurl="http://10.108.165.119:8080/Robot_Daring/touxiang_"+userid+".jpg";
	private final String path ="Robot_Daring/";

	public Handler hd=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				FileUtils fu=new FileUtils();
				Bitmap bm=fu.getImageFromSD(Environment.getExternalStorageDirectory() + "/"+path+"touxiang_"+userid+".jpg");
				imageView.setBackgroundDrawable(new BitmapDrawable(toRoundBitmap(bm)));
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changelogo);
		init();
		initimagetd.start();
	}

	private void init() {
		imageView = (ImageView) findViewById(R.id.imageView);
		imageView.setBackgroundDrawable(new BitmapDrawable(toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.me))));
		//imageView.setBackgroundResource(R.drawable.me);
		chosePic = (Button) findViewById(R.id.chosePic);
		uploadPic = (Button) findViewById(R.id.uploadPic);
		
		chosePic.setOnClickListener(this);
		uploadPic.setOnClickListener(this);
	}

	Thread initimagetd =new Thread(){
		public void run(){
			for(int i=0;i<3;i++){
			try {
				initimagetd.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			HttpDownloader httpdownloader=new HttpDownloader();
			int flag=-1;
			flag=httpdownloader.downFile(imageurl, path, "touxiang_"+userid+".jpg", true);
			
			if(flag!=-1){
				Message msg=new Message();
				msg.what=0;
				hd.sendMessage(msg);
			}
			
			
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId() ) {
		case R.id.chosePic:
			ShowPickDialog();
			break;
		case R.id.uploadPic:
			uploadPic();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 选择提示对话框
	 */
	private void ShowPickDialog() {
		new AlertDialog.Builder(this)
				.setTitle("设置头像...")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						/**
						 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
						 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
						 */
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						
						/**
						 * 下面这句话，与其它方式写是一样的效果，如果：
						 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						 * intent.setType(""image/*");设置数据类型
						 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
						 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
						 */
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 1);

					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						/**
						 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
						 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
						 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
						 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
						 */
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						//下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										"touxiang.jpg")));
						startActivityForResult(intent, 2);
					}
				}).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)throws java.lang.NullPointerException {
		switch (requestCode) {
		// 如果是直接从相册获取,一定要捕捉空指针异常，因为用户中途放弃更改图像，会包空指针错误
		case 1:
			try{
			Uri uri=data.getData();
			startPhotoZoom(uri);}
			catch(java.lang.NullPointerException e){
				Toast.makeText(getApplicationContext(), "你放弃了更改头像", Toast.LENGTH_LONG);
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()+ "/touxiang.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话，
			 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只
			 * 在这个地方加下，大家可以根据不同情况在合适的
			 * 地方做判断处理类似情况
			 * 
			 */
			if(data != null){
				dataIntent = data;
				setPicToView(data);
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
		 * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
		 * 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高，单位：像素
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	/**
     * 把bitmap转成圆形
    * */
    public Bitmap toRoundBitmap(Bitmap bitmap){
            int width=bitmap.getWidth();
            int height=bitmap.getHeight();
            int r=0;
            //取最短边做边长
           if(width<height){
                    r=width;
            }else{
                    r=height;
            }
            //构建一个bitmap
            Bitmap backgroundBm=Bitmap.createBitmap(width,height,Config.ARGB_8888);
            //new一个Canvas，在backgroundBmp上画图 
           Canvas canvas=new Canvas(backgroundBm);
            Paint p=new Paint();
            //设置边缘光滑，去掉锯齿 
           p.setAntiAlias(true);
            RectF rect=new RectF(0, 0, r, r);
            //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，  
            //且都等于r/2时，画出来的圆角矩形就是圆形 
           canvas.drawRoundRect(rect, r/2, r/2, p);
            //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉,这里用圆和背景相交，把背景裁剪成一块圆
           p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //canvas将bitmap画在backgroundBmp上
           canvas.drawBitmap(bitmap, null, rect, p);
            return backgroundBm;
    }
	/**
	 * 在裁剪后的方形图片基础上再裁剪为原型显示出来
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(toRoundBitmap(photo));
			imageView.setBackgroundDrawable(drawable);
		}
	}
	
	private void uploadPic(){
		if(dataIntent != null){
			Bundle extras = dataIntent.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				
				/**
				 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
				 * 传到服务器，QQ头像上传采用的方法跟这个类似
				 */
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] bytes = stream.toByteArray();
				// 将图片流以字符串形式存储下来
				
				final String picStr = new String(Base64Coder.encodeLines(bytes));
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("picStr", picStr));
						params.add(new BasicNameValuePair("picName", "touxiang_"+userid));
						final String result = NetWorkUtil.httpPost(url,params);
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(ChangeLogoActivity.this, result, Toast.LENGTH_SHORT).show();
							}
						});
					}
				}).start();
				//initimagetd.start();
				//如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
				//为我们可以用的图片类型就OK啦...吼吼
				//Bitmap dBitmap = BitmapFactory.decodeFile(tp);
				//Drawable drawable = new BitmapDrawable(dBitmap);
				
			}
		}else {
			Toast.makeText(ChangeLogoActivity.this, "图片不存在", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	

}
