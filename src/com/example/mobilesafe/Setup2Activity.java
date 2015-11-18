package com.example.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class Setup2Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}

	@Override
	protected void showNext() {
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		//要求在finish方法或者startActivity（intent）后面执行
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	protected void showPre() {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
		//要求在finish方法或者startActivity（intent）后面执行
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
}
