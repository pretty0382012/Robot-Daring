package com.john.test;

import com.john.utils.HttpUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestHttpUtils extends AndroidTestCase
{
	public void testSendInfo()
	{
		String res = HttpUtils.doGet("���ҽ���Ц��");
		Log.e("TAG", res);
		res = HttpUtils.doGet("���ҽ���������");
		Log.e("TAG", res);
		res = HttpUtils.doGet("���");
		Log.e("TAG", res);
		res = HttpUtils.doGet("������");
		Log.e("TAG", res);
	}
}