package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;

public class Setup4Activity extends BaseSetupActivity {
	
	private CheckBox cb_setup_start;
	
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		cb_setup_start = (CheckBox) findViewById(R.id.cb_setup_start);
		boolean isConfig = sp.getBoolean("start", false);
		cb_setup_start.setChecked(isConfig);
	}
	
	@Override
	protected void showNext() {
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.putBoolean("start", cb_setup_start.isChecked());
		editor.commit();
		Intent intent = new Intent(this, LostFindActivity.class);
		startActivity(intent);
		finish();
		//要求在finish方法或者startActivity（intent）后面执行
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	protected void showPre() {
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		//要求在finish方法或者startActivity（intent）后面执行
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
}
