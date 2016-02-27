package com.john;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class EditTextActivity extends Activity {

	private EditText msgedt=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgedt);
		 //�ӵ�һ��activity���ղ���
	    Intent intent = getIntent();
	    Bundle bundle = intent.getExtras();
	    String msg = "";
	    int res=0;
	    res=bundle.getInt("resid");
	    msg=bundle.getString("msg");
		msgedt=(EditText)findViewById(R.id.msgedt);
		msgedt.setBackgroundResource(res);
		InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(msgedt.getWindowToken(), 0); //myEdit�����EditText����
		msgedt.setText(msg);
		
		
	}
	
	
	
	
}
