package com.example.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {
	
	protected static final String TAG = "Setup1Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	protected void showNext() {
		// 下一步的点击事件
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		// 要求在finish方法或者startActivity（intent）后面执行
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}


	@Override
	protected void showPre() {
		
	}
	
}
