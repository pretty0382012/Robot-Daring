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
	 * ѡ����ʾ�Ի���
	 */
	private void ShowPickDialog() {
		new AlertDialog.Builder(this)
				.setTitle("����ͷ��...")
				.setNegativeButton("���", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						/**
						 * �տ�ʼ�����Լ�Ҳ��֪��ACTION_PICK�Ǹ���ģ�����ֱ�ӿ�IntentԴ�룬
						 * ���Է�������ܶණ����Intent�Ǹ���ǿ��Ķ��������һ����ϸ�Ķ���
						 */
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						
						/**
						 * ������仰����������ʽд��һ����Ч���������
						 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						 * intent.setType(""image/*");������������
						 * ���������Ҫ�����ϴ�����������ͼƬ����ʱ����ֱ��д�磺"image/jpeg �� image/png�ȵ�����"
						 * ����ط�С���и����ʣ�ϣ�����ֽ���£������������URI������ΪʲôҪ��������ʽ��дѽ����ʲô����
						 */
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 1);

					}
				})
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						/**
						 * ������仹�������ӣ����ÿ������չ��ܣ�����Ϊʲô�п������գ���ҿ��Բο����¹ٷ�
						 * �ĵ���you_sdk_path/docs/guide/topics/media/camera.html
						 * �Ҹտ���ʱ����Ϊ̫�������濴����ʵ�Ǵ�ģ�����������õ�̫���ˣ����Դ�Ҳ�Ҫ��Ϊ
						 * �ٷ��ĵ�̫���˾Ͳ����ˣ���ʵ�Ǵ�ģ�����ط�С��Ҳ���ˣ��������
						 */
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						//�������ָ������������պ����Ƭ�洢��·��
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
		// �����ֱ�Ӵ�����ȡ,һ��Ҫ��׽��ָ���쳣����Ϊ�û���;��������ͼ�񣬻����ָ�����
		case 1:
			try{
			Uri uri=data.getData();
			startPhotoZoom(uri);}
			catch(java.lang.NullPointerException e){
				Toast.makeText(getApplicationContext(), "������˸���ͷ��", Toast.LENGTH_LONG);
			}
			break;
		// ����ǵ����������ʱ
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()+ "/touxiang.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// ȡ�òü����ͼƬ
		case 3:
			/**
			 * �ǿ��жϴ��һ��Ҫ��֤���������֤�Ļ���
			 * �ڼ���֮��������ֲ����⣬Ҫ���²ü�������
			 * ��ǰ����ʱ���ᱨNullException��С��ֻ
			 * ������ط����£���ҿ��Ը��ݲ�ͬ����ں��ʵ�
			 * �ط����жϴ����������
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
	 * �ü�ͼƬ����ʵ��
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * �����������Intent��ACTION����ô֪���ģ���ҿ��Կ����Լ�·���µ�������ҳ
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * ֱ��������Ctrl+F�ѣ�CROP ��֮ǰС��û��ϸ��������ʵ��׿ϵͳ���Ѿ����Դ�ͼƬ�ü�����,
		 * ��ֱ�ӵ����ؿ�ģ�С����C C++  ���������ϸ�˽�ȥ�ˣ������Ӿ������ӣ������о���������ô
		 * ��������...���
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//�������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ��ߣ���λ������
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	/**
     * ��bitmapת��Բ��
    * */
    public Bitmap toRoundBitmap(Bitmap bitmap){
            int width=bitmap.getWidth();
            int height=bitmap.getHeight();
            int r=0;
            //ȡ��̱����߳�
           if(width<height){
                    r=width;
            }else{
                    r=height;
            }
            //����һ��bitmap
            Bitmap backgroundBm=Bitmap.createBitmap(width,height,Config.ARGB_8888);
            //newһ��Canvas����backgroundBmp�ϻ�ͼ 
           Canvas canvas=new Canvas(backgroundBm);
            Paint p=new Paint();
            //���ñ�Ե�⻬��ȥ����� 
           p.setAntiAlias(true);
            RectF rect=new RectF(0, 0, r, r);
            //ͨ���ƶ���rect��һ��Բ�Ǿ��Σ���Բ��X�᷽��İ뾶����Y�᷽��İ뾶ʱ��  
            //�Ҷ�����r/2ʱ����������Բ�Ǿ��ξ���Բ�� 
           canvas.drawRoundRect(rect, r/2, r/2, p);
            //���õ�����ͼ���ཻʱ��ģʽ��SRC_INΪȡSRCͼ���ཻ�Ĳ��֣�����Ľ���ȥ��,������Բ�ͱ����ཻ���ѱ����ü���һ��Բ
           p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //canvas��bitmap����backgroundBmp��
           canvas.drawBitmap(bitmap, null, rect, p);
            return backgroundBm;
    }
	/**
	 * �ڲü���ķ���ͼƬ�������ٲü�Ϊԭ����ʾ����
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
				 * ����ע�͵ķ����ǽ��ü�֮���ͼƬ��Base64Coder���ַ���ʽ��
				 * ������������QQͷ���ϴ����õķ������������
				 */
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] bytes = stream.toByteArray();
				// ��ͼƬ�����ַ�����ʽ�洢����
				
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
				//������ص��ķ����������ݻ�����Base64Coder����ʽ�Ļ������������·�ʽת��
				//Ϊ���ǿ����õ�ͼƬ���;�OK��...���
				//Bitmap dBitmap = BitmapFactory.decodeFile(tp);
				//Drawable drawable = new BitmapDrawable(dBitmap);
				
			}
		}else {
			Toast.makeText(ChangeLogoActivity.this, "ͼƬ������", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	

}
