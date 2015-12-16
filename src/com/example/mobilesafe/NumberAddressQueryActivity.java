package com.example.mobilesafe;

import com.example.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {
	
	private static final String TAG = "NumberAddressQueryActivity";
	private EditText et_phone;
	private TextView tv_result;
	
	
	//振动器
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		et_phone = (EditText) findViewById(R.id.et_phone);
		tv_result = (TextView) findViewById(R.id.tv_result);
		et_phone.addTextChangedListener(new TextWatcher() {
			
			//文本发生变化时的回调
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s!=null && s.length()>=3){
					//查询数据库并且显示结果
					String address = NumberAddressQueryUtils.queryNumber(s.toString());
					tv_result.setText(address);
				}
			}
			//文本发生变化前回调
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			//文本发生变化后回调
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	/**
	 * 查询号码归属地
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "号码为空", 0).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_phone.startAnimation(shake);
			//当电话号码为空的时候，就去震动手机提醒用户
//			vibrator.vibrate(2000);
			//停200震动200停300震动3000停1000震动2000
			long[] pattern = {200,200,300,3000,1000,2000};
			//-1表示不重复 0表示循环震动
			vibrator.vibrate(pattern, -1);
			return;
		}else{
			//去数据库查询号码归属地
			//1、网络查询2、本地的数据库
			//写一个工具类查询数据库
			Log.i(TAG, "您要查询的电话号码:"+phone);
			String address = NumberAddressQueryUtils.queryNumber(phone);
			tv_result.setText(address);
		}
		
	}
}
