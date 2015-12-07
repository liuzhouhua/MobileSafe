package com.example.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {
	
	private static final String TAG = "NumberAddressQueryActivity";
	private EditText et_phone,tv_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		et_phone = (EditText) findViewById(R.id.et_phone);
		tv_result = (EditText) findViewById(R.id.tv_result);
	}
	
	/**
	 * 查询号码归属地
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "号码为空", 0).show();
			return;
		}else{
			//去数据库查询号码归属地
			Log.i(TAG, "您要查询的电话号码:"+phone);
		}
		
	}
}
